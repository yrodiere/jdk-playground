package org.hibernate.jpamodelgen;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.Set;

@SupportedAnnotationTypes({
        "java.lang.Deprecated"
})
public class JPAMetaModelEntityProcessor extends AbstractProcessor {
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnvironment) {
        for (Element element : elementUtils.getModuleElement("").getEnclosedElements()) {
            if (element.getKind() == ElementKind.PACKAGE) {
                PackageElement pack = (PackageElement) element;
                for (Element packageMember : pack.getEnclosedElements()) {
                }
            }
        }
        return true;
    }

}
