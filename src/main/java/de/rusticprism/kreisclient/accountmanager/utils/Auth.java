package de.rusticprism.kreisclient.accountmanager.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mojang.util.UUIDTypeAdapter;
import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.accountmanager.account.AuthException;
import net.minecraft.text.Text;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Auth {
    /**
     * Process <code>Authorization Code -> Authorization Token</code> step.
     * @param code Code from user auth redirect
     * @return Pair of <code>[access_token,refresh_token]</code> (Auth Token, Refresh Token) from JSON response
     * @throws IllegalArgumentException If server response is not HTTP Success (200-299)
     * @throws Exception If something goes wrong
     * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Authorization_Code_-.3E_Authorization_Token">...</a>
     */
    public static Pair<String, String> authCode2Token(String code) throws IllegalArgumentException, Exception {
		Request pr = new Request("https://login.live.com/oauth20_token.srf");
		pr.header("Content-Type", "application/x-www-form-urlencoded");
        HashMap<Object, Object> req = new HashMap<>();
        req.put("client_id", "54fd49e4-2103-4044-9603-2b028c814ec3");
        req.put("code", code);
        req.put("grant_type", "authorization_code");
        req.put("redirect_uri", "http://localhost:59125");
        req.put("scope", "XboxLive.signin XboxLive.offline_access");
        pr.post(req); //Note: Here we're encoding parameters as HTTP. (key=value)
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("authCode2Token response: " + pr.response());
        JsonObject resp = KreisClient.INSTANCE.GSON.fromJson(pr.body(), JsonObject.class);
        return Pair.of(resp.get("access_token").getAsString(), resp.get("refresh_token").getAsString());
    }
    
    /**
     * Process <code>Refreshing Tokens</code> step.
     * @param refreshToken Refresh token from {@link #authCode2Token(String)} or from this method
     * @return Pair of <code>[access_token,refresh_token]</code> (Auth Token, Refresh Token) from JSON response
     * @throws IllegalArgumentException If server response is not HTTP Success (200-299)
     * @throws Exception If something goes wrong
     * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Refreshing_Tokens">...</a>
     */
    public static Pair<String, String> refreshToken(String refreshToken) throws IllegalArgumentException, Exception {
    	Request r = new Request("https://login.live.com/oauth20_token.srf");
		r.get();
		Map<Object, Object> req = new HashMap<>();
		req.put("client_id", "54fd49e4-2103-4044-9603-2b028c814ec3");
		req.put("refresh_token", refreshToken);
		req.put("grant_type", "refresh_token");
		req.put("redirect_uri", "http://localhost:59125");
		r.post(req); //Note: Here we're encoding parameters as HTTP. (key=value)
		if (r.response() < 200 || r.response() >= 300) throw new IllegalArgumentException("refreshToken response: " + r.response());
		JsonObject resp = KreisClient.INSTANCE.GSON.fromJson(r.body(), JsonObject.class);
		return Pair.of(resp.get("access_token").getAsString(), resp.get("refresh_token").getAsString());
    }
    
    /**
     * Process <code>Authenticate with XBL</code> step.
     * @param authToken Authorization Token (<code>access_token</code>) from {@link #authCode2Token(String)} or {@link #refreshToken(String)}
     * @return The <code>Token</code> (XBL token) from JSON response
     * @throws IllegalArgumentException If server response is not HTTP Success (200-299)
     * @throws Exception If something goes wrong
     * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Authenticate_with_XBL">...</a>
     */
    public static String authXBL(String authToken) throws IllegalArgumentException, Exception {
		Request pr = new Request("https://user.auth.xboxlive.com/user/authenticate");
		pr.header("Content-Type", "application/json");
		pr.header("Accept", "application/json");
		JsonObject req = new JsonObject();
		JsonObject reqProps = new JsonObject();
		reqProps.addProperty("AuthMethod", "RPS");
		reqProps.addProperty("SiteName", "user.auth.xboxlive.com");
		reqProps.addProperty("RpsTicket", "d=" + authToken);
		req.add("Properties", reqProps);
		req.addProperty("RelyingParty", "http://auth.xboxlive.com");
		req.addProperty("TokenType", "JWT");
        pr.post(req.toString()); //Note: Here we're encoding parameters as JSON. ('key': 'value')
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("authXBL response: " + pr.response());
        return KreisClient.INSTANCE.GSON.fromJson(pr.body(), JsonObject.class).get("Token").getAsString();
    }
    
    /**
     * Process <code>Authenticate with XSTS</code> step.
     * @param xblToken XBL Token from {@link #authXBL(String)}
     * @return Pair of <code>[Token,Userhash]</code> ([XSTS Token, XUI-UHS Userhash]) from JSON response
     * @throws de.rusticprism.kreisclient.accountmanager.account.AuthException If the account doesn't have an Xbox account, XboxLife is banned in country or you're under 18. (Expected exception)
     * @throws IllegalArgumentException If server response is not HTTP Success (200-299)
     * @throws Exception If something goes wrong
     * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Authenticate_with_XSTS">...</a>
     */
    public static Pair<String, String> authXSTS(String xblToken) throws AuthException, IllegalArgumentException, Exception {
		Request pr = new Request("https://xsts.auth.xboxlive.com/xsts/authorize");
		pr.header("Content-Type", "application/json");
		pr.header("Accept", "application/json");
		JsonObject req = new JsonObject();
		JsonObject reqProps = new JsonObject();
		JsonArray userTokens = new JsonArray();
		userTokens.add(xblToken);
		reqProps.add("UserTokens", userTokens); //Singleton JSON Array.
		reqProps.addProperty("SandboxId", "RETAIL");
        req.add("Properties", reqProps);
        req.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        req.addProperty("TokenType", "JWT");
        pr.post(req.toString()); //Note: Here we're encoding parameters as JSON. ('key': 'value')
        if (pr.response() == 401) throw new AuthException(Text.translatable("kreisclient.auth.error.noxbox"));
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("authXSTS response: " + pr.response());
        JsonObject resp = KreisClient.INSTANCE.GSON.fromJson(pr.body(), JsonObject.class);
        return Pair.of(resp.get("Token").getAsString(), resp.getAsJsonObject("DisplayClaims")
        		.getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString());
    }
    
    /**
     * Process <code>Authenticate with Minecraft</code> step.
     * @param userHash XUI-UHS Userhash from {@link #authXSTS(String)}
     * @param xstsToken XSTS Token from {@link #authXSTS(String)}
     * @return The <code>access_token</code> (Minecraft access token) from JSON response
     * @throws IllegalArgumentException If server response is not HTTP Success (200-299)
     * @throws Exception If something goes wrong
     * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Authenticate_with_Minecraft">...</a>
     */
    public static String authMinecraft(String userHash, String xstsToken) throws IllegalArgumentException, Exception {
		Request pr = new Request("https://api.minecraftservices.com/authentication/login_with_xbox");
		pr.header("Content-Type", "application/json");
		pr.header("Accept", "application/json");
		JsonObject req = new JsonObject();
        req.addProperty("identityToken", "XBL3.0 x=" + userHash + ";" + xstsToken);
        pr.post(req.toString()); //Note: Here we're encoding parameters as JSON. ('key': 'value')
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("authMinecraft response: " + pr.response());
        return KreisClient.INSTANCE.GSON.fromJson(pr.body(), JsonObject.class).get("access_token").getAsString();
    }
    
    /**
     * Process <code>Checking Game Ownership</code> step.
     * @param accessToken Minecraft access token from {@link #authMinecraft(String, String)}
     * @throws AuthException If the account doesn't own the game (Expected exception)
     * @throws IllegalArgumentException If server response is not HTTP Success (200-299)
     * @throws Exception If something goes wrong
     * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Checking_Game_Ownership">...</a>
     */
    public static void checkGameOwnership(String accessToken) throws AuthException, IllegalArgumentException, Exception {
		Request pr = new Request("https://api.minecraftservices.com/entitlements/mcstore");
		pr.header("Authorization", "Bearer " + accessToken);
		pr.get(); //Note: Here we're using GET, not POST.
		if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("checkGameOwnership response: " + pr.response());
        if (KreisClient.INSTANCE.GSON.fromJson(pr.body(), JsonObject.class).getAsJsonArray("items").size() == 0) throw new AuthException(Text.translatable("ias.msauth.error.gamenotowned"));
    }
    
    /**
     * Process <code>Get the profile</code> step.
     * @param accessToken Minecraft access token from {@link #authMinecraft(String, String)}
     * @return Pair of <code>[id,name]</code> (UUID, Playername) from JSON response
     * @throws IllegalArgumentException If server response is not HTTP Success (200-299)
     * @throws Exception If something goes wrong
     * @see <a href="https://wiki.vg/Microsoft_Authentication_Scheme#Get_the_profile">...</a>
     */
    public static Pair<UUID, String> getProfile(String accessToken) throws IllegalArgumentException, Exception {
		Request pr = new Request("https://api.minecraftservices.com/minecraft/profile");
		pr.header("Authorization", "Bearer " + accessToken);
		pr.get(); //Note: Here we're using GET, not POST.
        if (pr.response() < 200 || pr.response() >= 300) throw new IllegalArgumentException("getProfile response: " + pr.response());
        JsonObject resp = KreisClient.INSTANCE.GSON.fromJson(pr.body(), JsonObject.class);
        return Pair.of(UUIDTypeAdapter.fromString(resp.get("id").getAsString()), resp.get("name").getAsString());
    }
    
    /**
     * Perform authentication using Mojang auth system.
     * @param name Player login (usually email)
     * @param pwd Player password
     * @return Authorized Mojang account
     * @throws AuthException If auth exception occurs (Invalid login/pass, Too fast login, Account migrated to Microsoft, etc.)
     * @throws IOException If connection exception occurs
     */
}
