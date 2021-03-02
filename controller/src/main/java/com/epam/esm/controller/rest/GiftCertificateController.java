package com.epam.esm.controller.rest;

import com.epam.esm.model.common.FilterParams;
import com.epam.esm.model.common.SortType;
import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.GiftCertificateService;
import com.epam.esm.model.service.dto.CertificateDTO;
import com.epam.esm.model.service.exception.ServiceException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Gift certificate RestAPI.
 */

@RestController
@RequestMapping("/certificates")
public class GiftCertificateController {

    private final GiftCertificateService certificateService;

    public GiftCertificateController(GiftCertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Find certificate by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @GetMapping("/{id}")
    public ResponseEntity<CertificateDTO> find(@PathVariable(name = "id") Long id)
            throws ServiceException, NotExistEntityException {
        CertificateDTO result = certificateService.find(id);
        return ResponseEntity.ok(result);
    }

    /**
     * Add certificate.
     *
     * @param certificateDTO the certificate dto
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @PostMapping
    public ResponseEntity<CertificateDTO> add(@Valid @RequestBody CertificateDTO certificateDTO) throws ServiceException {
        CertificateDTO result = certificateService.add(certificateDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Update certificate. Mark the fields that are not specified for updating null.
     *
     * @param certificateDTO the certificate dto
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @PutMapping(consumes = "application/json")
    public ResponseEntity<CertificateDTO> update(@RequestBody CertificateDTO certificateDTO)
            throws ServiceException, NotExistEntityException {
        CertificateDTO result = certificateService.update(certificateDTO);
        return ResponseEntity.ok(result);
    }

    /**
     * Delete certificate by id.
     *
     * @param id the id
     * @return the response entity
     * @throws ServiceException the service exception
     */
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") Long id)
            throws ServiceException, NotExistEntityException {
        certificateService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Filter gift certificates by parameters
     *
     * @param tags    tag or tags with &(and) condition
     * @param part   part od name or description
     * @param sortBy sort type
     * @param type   sort by name or by date
     * @param page   page number
     * @param size   elements on page
     * @return the response entity
     * @throws ServiceException           service exception
     * @throws IncorrectArgumentException incorrect argument exception
     */
    @GetMapping
    public ResponseEntity<PagedModel<CertificateDTO>> filterByParameter(
            @RequestParam(value = "tags", required = false) List<String> tags,
            @RequestParam(value = "part", required = false) String part,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "type", required = false) SortType type,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "3") int size
    ) throws ServiceException, IncorrectArgumentException {
        FilterParams filterParams = FilterParams.builder()
                .tags(tags)
                .part(part)
                .sortBy(sortBy)
                .type(type)
                .build();
        List<CertificateDTO> list = certificateService.filterByParameters(filterParams, page, size);
        long count = certificateService.count();
        PagedModel.PageMetadata pageMetadata = new PagedModel.PageMetadata(size, page, count);
        List<Link> linkList = buildLink(tags, part, sortBy, type, page, size, pageMetadata.getTotalPages());
        PagedModel<CertificateDTO> pagedModel = PagedModel.of(list, pageMetadata, linkList);
        return ResponseEntity.ok(pagedModel);
    }

    private List<Link> buildLink(List<String> tags, String part, String sortBy, SortType type, int page, int size,
                                 long maxPage) throws ServiceException, IncorrectArgumentException {
        List<Link> linkList = new ArrayList<>();
        Link self = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder
                        .methodOn(GiftCertificateController.class)
                        .filterByParameter(tags, part, sortBy, type, page, size)
        ).withRel("self").expand();
        linkList.add(self);
        if (page > 1) {
            Link previous = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder
                            .methodOn(GiftCertificateController.class)
                            .filterByParameter(tags, part, sortBy, type, page - 1, size)
            ).withRel("previous").expand();
            linkList.add(previous);
        }
        if (maxPage > page) {
            Link next = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder
                            .methodOn(GiftCertificateController.class)
                            .filterByParameter(tags, part, sortBy, type, page + 1, size)
            ).withRel("next").expand();
            linkList.add(next);
        }
        return linkList;
    }
}
