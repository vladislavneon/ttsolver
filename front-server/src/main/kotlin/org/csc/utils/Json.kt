package org.csc.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import kotlin.reflect.KClass

object Json {
    private val jsonMapper = ObjectMapper().also {
        it.registerModule(KotlinModule())
        it.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        it.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        it.setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    fun writeValueAsString(obj: Any): String = jsonMapper.writeValueAsString(obj)

    fun <T : Any> readValue(serialized: String, klass: KClass<T>): T = jsonMapper.readValue<T>(serialized, klass.java)

    fun <T : Any> readValue(serialized: String, typeInfo: TypeReference<T>): T = jsonMapper.readValue(serialized, typeInfo)

    fun <T : Any> registerEntity(klass: KClass<T>, serializer: StdSerializer<T>, deserializer: StdDeserializer<T>) {
        val customModule = SimpleModule(klass.qualifiedName)
        customModule.addSerializer(serializer)
        customModule.addDeserializer(klass.java, deserializer)
        jsonMapper.registerModule(customModule)
    }

    fun <E : Collection<T>, T : Any> readCollectionValue(serialized: String, collection: KClass<E>, klass: KClass<T>): E {
        val type = TypeFactory.defaultInstance()
        return jsonMapper.readValue(serialized, type.constructCollectionType(collection.java, klass.java))
    }

    fun <E : Map<T, K>, T : Any, K : Any> readMapValue(serialized: String, mapKlass: KClass<E>, keyKlass: KClass<T>, valueKlass: KClass<K>): E {
        val type = TypeFactory.defaultInstance()
        return jsonMapper.readValue(serialized, type.constructMapType(mapKlass.java, keyKlass.java, valueKlass.java))
    }
}
