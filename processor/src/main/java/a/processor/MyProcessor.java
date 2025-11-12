package a.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ModuleElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.Set;

@SupportedAnnotationTypes({"a.Processed"})
@SupportedOptions({})
public class MyProcessor extends AbstractProcessor {

    private Elements elementUtils;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        triggerProblem(elementUtils.getModuleElement(""));
        return true;
    }

    private static TypeElement triggerProblem(ModuleElement module) {
        for (Element element : module.getEnclosedElements()) {
            if (element.getKind() == ElementKind.PACKAGE) {
                var pack = (PackageElement) element;
                for (var element2 : pack.getEnclosedElements()) { // This could trigger problem 1, in the right conditions.
                    // When problem 1 does not happen, it's unclear whether the loading of `element` happened correctly,
                    // so there might be a problem 2 hiding just beneath the surface... ?
                }
            }
        }
        return null;
    }
}
