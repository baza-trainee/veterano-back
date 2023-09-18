package com.zdoryk.data.url;

import com.zdoryk.data.exception.RedirectException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UrlService {

    private final UrlRepository urlRepository;


    public void saveUrl(Url url){
        urlRepository.save(url);
    }


    @SneakyThrows
    public URI redirectById(UUID id){
        System.out.println(id);
        Url uri = urlRepository.getUrlById(id)
                .orElseThrow(() -> new RedirectException("By this id does not exist url"));

        uri.setVisitors(uri.getVisitors() + 1L);
        urlRepository.save(uri);
        return new URI("https://"+uri.getUrl());
    }

    public Optional<Url> findByUrlString(String url){
        return urlRepository.getUrlByUrl(url);
    }

    public void deleteUrl(Url url){
        urlRepository.delete(url);
    }

}
