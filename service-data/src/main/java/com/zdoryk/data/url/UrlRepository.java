package com.zdoryk.data.url;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {

    Optional<Url> getUrlByUrl(String url);
    Optional<Url> getUrlById(UUID id);

}
