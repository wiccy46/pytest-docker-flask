package uk.neilgall.rulesapp

fun RuleSet<String>.resolve(): RuleSet<Attribute> {
    val attributesByName = attributes.associate({ it.name to it })
    val dynamicAttributes: MutableList<Attribute> = mutableListOf()

    fun lookup(name: String): Attribute {
        return attributesByName.getOrElse(name, {
            if (name.startsWith("\"") && name.endsWith("\"")) {
                // Promote quoted strings to constant attributes
                val a = Attribute.String("const${dynamicAttributes.size}", name.removeSurrounding("\""))
                dynamicAttributes.add(a)
                a
            } else{
                throw NoSuchElementException(name)
            }
        })
    }

    return map({ lookup(it) }).plusAttributes(dynamicAttributes)
}
