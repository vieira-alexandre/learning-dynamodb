package dev.alexandrevieira.learningdynamodb.exception

import kotlin.reflect.KClass

class DuplicateKeyException(clazz: KClass<*>) : RuntimeException("This ${clazz.simpleName} already exists") {
}