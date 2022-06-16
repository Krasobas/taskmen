package com.krasobas.task_manager.config;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class DispatcherInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {SpringConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }

    //теоретически этот метод автоматически регистрирует все внесенные фильтры
    // но, например, кодировка не работает без явно заданного мапинга
    // а как его здесь задать я не понял
    // одновременно с ручной загрузкой нельзя запустить (null где-то вылазит)
//    @Override
//    protected Filter[] getServletFilters() {
//        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
//        characterEncodingFilter.setEncoding("UTF-8");
//        characterEncodingFilter.setForceEncoding(true);
//        return new Filter[] {characterEncodingFilter};
//    }

    //здесь вручную добавляем фультры к сервлету и задаем вручную их маппинг
    @Override
    public void onStartup(ServletContext aServletContext) throws ServletException {
        super.onStartup(aServletContext);
        registerCharacterEncodingFilter(aServletContext);
        registerHiddenFieldFilter(aServletContext);
    }

    // это подключение фильтра для обработки скрытых HTTP методов (PATCH, DELETE)
    private void registerHiddenFieldFilter(ServletContext aServletContext) {
        aServletContext.addFilter("hiddenHttpMethodFilter", new HiddenHttpMethodFilter())
                .addMappingForUrlPatterns(null ,true, "/*");
    }

    //это позволит работать с русскими символами
    //в каждом html и у шаблонизатора  тоже должна быть явно прописана кодировка
    private void registerCharacterEncodingFilter(ServletContext aServletContext) {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        aServletContext.addFilter("characterEncodingFilter", characterEncodingFilter)
                .addMappingForUrlPatterns(null, false, "/*");
    }
}
