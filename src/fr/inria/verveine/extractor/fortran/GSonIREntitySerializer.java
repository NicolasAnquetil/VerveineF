package fr.inria.verveine.extractor.fortran;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import fr.inria.verveine.extractor.fortran.ir.IREntity;

public class GSonIREntitySerializer implements JsonSerializer<IREntity> {

	@Override
	public JsonElement serialize(IREntity entity, Type clazz, JsonSerializationContext jsonCtxt) {
        JsonObject jsonObj = new JsonObject();
        String parentKey = null;

        if (entity.getParent() != null) {
        	parentKey = entity.getParent().getKey();
        }
        jsonObj.addProperty("key", entity.getKey());
        jsonObj.addProperty("parent", parentKey);
        jsonObj.add("kind", jsonCtxt.serialize(entity.getKind()));
        jsonObj.add("data", jsonCtxt.serialize(entity.getData()));

        return jsonObj;
	}

}
