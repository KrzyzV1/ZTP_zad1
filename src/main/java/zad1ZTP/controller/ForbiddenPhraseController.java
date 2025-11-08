package zad1ZTP.controller;

import zad1ZTP.model.ForbiddenPhrase;
import zad1ZTP.service.ForbiddenPhraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/forbidden-phrases")
@RequiredArgsConstructor
public class ForbiddenPhraseController {
    private final ForbiddenPhraseService service;

    @GetMapping
    public List<ForbiddenPhrase> list() {
        return service.listAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ForbiddenPhrase add(@RequestBody ForbiddenPhrase phrase) {
        return service.add(phrase);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
