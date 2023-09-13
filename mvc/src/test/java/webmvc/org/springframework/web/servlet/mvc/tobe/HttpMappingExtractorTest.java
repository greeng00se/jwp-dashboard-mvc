package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import samples.TestController;
import web.org.springframework.web.bind.annotation.PostMapping;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class HttpMappingExtractorTest {

    private final HttpMappingExtractor httpMappingExtractor = new HttpMappingExtractor();

    @Test
    void Annotation을_입력받아_RequestUri을_추출한다() throws NoSuchMethodException {
        // given
        final Method method = TestController.class.getDeclaredMethod("findUserId", HttpServletRequest.class,
                HttpServletResponse.class);
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        // when
        final String uri = httpMappingExtractor.extractRequestUri(requestMapping);

        // then
        assertThat(uri).isEqualTo("/request");
    }

    @Test
    void RequestMappingAnnotation을_입력받아_RequestUri을_추출한다() throws NoSuchMethodException {
        // given
        final Method method = TestController.class.getDeclaredMethod(
                "findUserId",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        final RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

        // when
        final RequestMethod[] requestMethods = httpMappingExtractor.extractRequestMethod(requestMapping);

        // then
        assertThat(requestMethods).containsExactly(RequestMethod.GET);
    }

    @Test
    void RequestMapping이_아닌_다른_HttpMappingAnnotation을_입력받는_경우_MetaAnnotation에_있는_RequestMapping의_정보를_추출한다()
            throws NoSuchMethodException {
        // given
        final Method method = TestController.class.getDeclaredMethod(
                "save",
                HttpServletRequest.class,
                HttpServletResponse.class
        );
        final PostMapping postMapping = method.getDeclaredAnnotation(PostMapping.class);

        // when
        final RequestMethod[] requestMethods = httpMappingExtractor.extractRequestMethod(postMapping);

        // then
        assertThat(requestMethods).containsExactly(RequestMethod.POST);
    }
}
