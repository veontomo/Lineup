package com.veontomo.lineup
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiMethod
//import org.jetbrains.kotlin.com.intellij.psi.PsiClass
//import org.jetbrains.kotlin.com.intellij.psi.PsiElement
//import org.jetbrains.kotlin.com.intellij.psi.PsiMethod
//import java.lang.Exception

//import org.jetbrains.kotlin.asJava.elements.KtLightElement
//import org.jetbrains.kotlin.psi.KtClass
//import org.jetbrains.kotlin.psi.KtElement


/**
 * Sort the class fields and methods in the canonical order.
 *
 * A class is said to be sorted canonically if its fields and methods are in this order:
 * 1. fields
 * 1. base methods
 * 2. other methods
 *
 * The order of the base methods is defined by array lineup.
 */
class CanonicalSorter(private val aClass: PsiClass, private val lineup: Array<String>) {

    private val notifier = Notifier("com.veontomo.lineup.CanonicalSorter")
    /**
     * Perform the ordering of the class methods and fields.
     */
    fun execute() {
        val methods = aClass.methods
        notifier.notify("methods: ${methods.joinToString { it.name }}")
        val fields = aClass.fields
        notifier.notify("fields: ${fields.joinToString { it.name ?: "nameless field" }}")
        val sorted = lineupFilter(methods, lineup)
        notifier.notify("sorted: ${sorted.joinToString { it.name }}")
        val pivot = getFirstMethodOrField(aClass)?.navigationElement
        notifier.notify("first method or field received, it is ${if (pivot == null) "" else "not"} null")
        val parent = pivot?.parent?.navigationElement
        notifier.notify("parent: ${if (parent == null) "" else "not"} null")
        if (pivot != null && parent != null) {
            fields.forEach { addBefore(it, pivot, parent) }
//             place the lineup methods after the fields
            sorted.forEach { addBefore(it, pivot, parent) }
//             remove the above inserted elements in order to avoid duplicates
            fields.forEach { delete(it) }
            sorted.forEach { delete(it) }
        }
    }

    private fun addBefore(element: PsiElement, pivot: PsiElement, parent: PsiElement) {
        notifier.notify("adding ${element.text} before ${pivot.text}, its navigationElement is ${if (element.navigationElement == null) "" else "not"} null.")
        try {
            parent.addBefore(element.navigationElement, pivot)
        } catch (e:Exception ){
            notifier.notify("failed to add ${element.text} before ${pivot.text}, ${e.message}")
        }
    }

    private fun delete(elem: PsiElement) {
        notifier.notify("deleting ${elem.text}, its navigationElement is ${if (elem.navigationElement == null) "" else "not"} null.")
        elem.navigationElement.delete()
    }


    /**
     * Return a new array composed of the elements of the array "pool": only those methods from the array "pool"
     * end up in the resulting array whose names are present in the array "filter". The order of the elements in the
     * resulting array respects their order in tha array "filter": if the name of a method M1 comes before the name
     * of the method M2 in array "filter", then the method M1 must come before the method M2 in the resulting array.
     *
     * @param pool array of methods
     * @param filter array of method names defining what methods should be present in the output and in what order.
     * @return methods from the above array whose names are in the lineup; the order is defined above.
     */
    fun lineupFilter(pool: Array<PsiMethod>, filter: Array<String>): Array<PsiMethod> {
        val list = mutableListOf<PsiMethod>()
        for (methodName in filter) {
            pool.forEach { if (it.name == methodName) list.add(it) }
        }
        return list.toTypedArray()
    }


    /**
     * Return the first child that is either a field or a method.

     * @param aClass
     * @return a method or a field with the lowest offset
     */
    fun getFirstMethodOrField(aClass: PsiClass): PsiElement? {
        val methods = aClass.methods
        val fields = aClass.fields
        val methodTotal = methods.size
        val fieldTotal = fields.size
        notifier.notify("# methods: $methodTotal, # fields: $fieldTotal")
        if (methodTotal == 0 && fieldTotal == 0) return null
        if (methodTotal == 0) return fields[0]
        if (fieldTotal == 0) return methods[0]
        val firstMethodOffset = methods[0].navigationElement.startOffsetInParent
        val firstFieldOffset = fields[0].navigationElement.startOffsetInParent

        notifier.notify("first method offset: $firstMethodOffset, first field offset: $firstFieldOffset")
        return if (firstFieldOffset > firstMethodOffset) methods[0] else fields[0]
    }


}

