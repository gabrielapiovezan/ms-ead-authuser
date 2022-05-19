package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDTO;
import com.ead.authuser.dtos.ResponsePageDTO;
import com.ead.authuser.services.UtilsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@Component
public class CourseClient {

    private final RestTemplate restTemplate;

    private final UtilsService utilsService;


    @Value("${ead.api.url.course}")
    String REQUEST_URL_COURSE;

    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {

        List<CourseDTO> searchResult = null;
        String url = REQUEST_URL_COURSE + utilsService.utilsService(userId, pageable);
        log.debug("Rest Url {}", url);
        log.info("Rest Url {}", url);
        try {
            ParameterizedTypeReference<ResponsePageDTO<CourseDTO>> responseType = new ParameterizedTypeReference<ResponsePageDTO<CourseDTO>>() {
            };
            ResponseEntity<ResponsePageDTO<CourseDTO>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();

            log.debug("error {}", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request/ courses {}", e);
        }
        log.info("Ending request/ courses {}", userId);
        return new PageImpl<>(searchResult);
    }
}
