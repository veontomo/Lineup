package com.veontomo.lineup

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiClass
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

    private val notifier = Notifier("Lineup plugin");

    override
    fun actionPerformed(e: AnActionEvent) {
        notifier.notify("ciao")
//        final PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
//        PsiClass[] psiClasses = getPsiClasses(psiFile);
//        if (psiClasses != null) {
//            elaborateMultipleClasses(psiClasses);
//        } else {
//            notifier.notify("no class is found");
//        }

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
        val sorter = CanonicalSorter(aClass, BASE_METHOD_NAMES)
        sorter.execute()
//        WriteCommandAction.Simple(aClass.project, aClass.containingFile){

//        }
//        WriteCommandAction.Simple(aClass.project, aClass.containingFile) {
//
//        }.execute()
//        WriteCommandAction.Simple(aClass.getProject(), aClass.getContainingFile()) {
//            override
//            protected void run() throws Throwable {
//            }
//        }.execute();

    }


    /**
     * Determine classes that the currently open file contains
     *
     * @param psiFile current psi file
     * @return array of PsiClass instances
     */
    private fun getPsiClasses(psiFile: PsiFile): Array<PsiClass>? {

//        if (psiFile != null && psiFile instanceof PsiClassOwner) {
//            return ((PsiClassOwner) psiFile).getClasses();
//        }
        return null
    }
}