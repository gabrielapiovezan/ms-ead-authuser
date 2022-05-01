package com.ead.authuser.clients;

import com.ead.authuser.dto.CourseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@Component
public class UserClient {

    private final RestTemplate restTemplate;

    private final String REQUEST_URI = "http://localhost:8082";

    public Page<CourseDTO> getAllCoursesByUser(UUID userId, Pageable pageable) {
        List<CourseDTO> searchResult = null;
        String url = REQUEST_URI.concat("/courses/userId=").concat(userId.toString()).concat("&page=").concat(String.valueOf(pageable.getPageNumber())
                .concat("&size=").concat(String.valueOf(pageable.getPageSize())).concat("&sort=").concat(pageable.getSort().toString().replaceAll(":", ",")));

        log.debug("Rest Url {}", url);
        log.info("Rest Url {}", url);
        try {
            log.debug("error {}", searchResult.size());
        } catch (HttpStatusCodeException e) {
            log.error("Error request/ courses {}", e);
        }
        log.info("Ending request/ courses {}", userId);
        return null;
    }
}
