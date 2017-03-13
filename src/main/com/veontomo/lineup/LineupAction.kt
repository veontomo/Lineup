package com.veontomo.lineup
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiClassOwner
import com.intellij.psi.PsiFile

/**
 * Plugin for rearrange fields and methods of classes found in a file that is currently open in the
 * Intellij IDEA IDE.
 */
class LineupAction : AnAction() {
    /**
     * list of basic method names that should be ordered according to their indexes in the array:
     */
    private val BASE_METHOD_NAMES = arrayOf(
            "onAttach", "onCreate", "onCreateView", "onViewCreated", "onActivityCreated", "onViewStateRestored",
            "onRestoreInstanceState", "onRestart", "onStart", "onResume", "onPause", "onSaveInstanceState", "onStop",
            "onDestroyView", "onDestroy", "onDetach"
    )

    private val notifier = Notifier("Lineup plugin")

    override
    fun actionPerformed(e: AnActionEvent) {
        val psiFile = e.getData(LangDataKeys.PSI_FILE)
        if (psiFile != null) {
            val psiClasses = getPsiClasses(psiFile)
            elaborateMultipleClasses(psiClasses)
        } else {
            notifier.notify("no class is found")
        }

    }

    /**
     * Iterate over given array of classes and elaborate each class.
     *
     * @param classes array of classes
     */
    private fun elaborateMultipleClasses(classes: Array<PsiClass>) {
        classes.forEach { elaborateSingleClass(it) }
    }

    /**
     * Make a given class be ordered canonically.
     *
     * @param aClass a class whose methods and fields are to be ordered.
     */
    private fun elaborateSingleClass(aClass: PsiClass) {
        Worker().execute(aClass, BASE_METHOD_NAMES)
    }


    /**
     * Determine classes that the currently open file contains
     *
     * @param psiFile current psi file
     * @return array of PsiClass instances
     */
    private fun getPsiClasses(psiFile: PsiFile): Array<PsiClass> {
        if (psiFile is PsiClassOwner) {
            return psiFile.classes
        }
        return arrayOf()
    }
}