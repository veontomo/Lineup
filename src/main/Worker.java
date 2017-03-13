
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.psi.PsiClass;

/**
 * A java class that encapsulates a call to WriteCommandAction that
 * can not be converted to a kotlin code for the moment.
 */
public class Worker {
    public void execute(final PsiClass aClass, final String[] methods){
        new WriteCommandAction.Simple(aClass.getProject(), aClass.getContainingFile()) {
            @Override
            protected void run() throws Throwable {
                CanonicalSorter sorter = new CanonicalSorter(aClass, methods);
                sorter.execute();
            }
        }.execute();
    }
}
