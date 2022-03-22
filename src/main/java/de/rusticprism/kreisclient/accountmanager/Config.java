package de.rusticprism.kreisclient.accountmanager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.accountmanager.account.Account;
import net.minecraft.client.MinecraftClient;

public class Config {
	public static final transient Gson GSON = new GsonBuilder().excludeFieldsWithModifiers(Modifier.TRANSIENT).registerTypeAdapter(Account.class, new IASJsonSerializer()).create();

	public static String textX;
	public static String textY;
	public static List<Account> accounts = new ArrayList<>();

	public static class IASJsonSerializer implements JsonSerializer<Account>, JsonDeserializer<Account> {
		@Override
		public Account deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			try {
				JsonObject jo = json.getAsJsonObject();
				Class<?> type = Class.forName(jo.get("type").getAsString());
				return context.deserialize(jo.get("data"), type);
			} catch (Throwable t) {
				throw new JsonParseException("Unable to parse account: " + json, t);
			}
		}

		@Override
		public JsonElement serialize(Account src, Type typeOfSrc, JsonSerializationContext context) {
			JsonObject jo = new JsonObject();
			jo.addProperty("type", src.getClass().getName());
			jo.add("data", context.serialize(src));
			return jo;
		}
	}
}
