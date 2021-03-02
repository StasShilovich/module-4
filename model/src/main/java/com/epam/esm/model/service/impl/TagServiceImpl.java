package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.service.Page;
import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.NotExistEntityException;
import com.epam.esm.model.service.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final TagDTOMapper dtoMapper;

    public TagServiceImpl(TagDao tagDao, TagDTOMapper dtoMapper) {
        this.tagDao = tagDao;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public TagDTO find(Long id) throws ServiceException {
        try {
            Optional<Tag> tag = tagDao.findById(id);
            return tag.map(dtoMapper::toDTO).orElseGet(TagDTO::new);
        } catch (DataAccessException e) {
            log.error("Tag with id=" + id + " not exist!", e);
            throw new ServiceException("Tag with id=" + id + " not exist!", e);
        }
    }

    @Override
    public TagDTO add(TagDTO tagDTO) throws ServiceException {
        try {
            Tag fromDTO = dtoMapper.fromDTO(tagDTO);
            Tag tag = tagDao.create(fromDTO);
            return dtoMapper.toDTO(tag);
        } catch (DataAccessException e) {
            log.error("Add tag service exception", e);
            throw new ServiceException("Add tag service exception", e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException, NotExistEntityException {
        try {
            tagDao.delete(id);
        } catch (DataAccessException e) {
            log.error("Delete tag service exception", e);
            throw new ServiceException("Delete tag service exception", e);
        }
    }

    @Override
    public List<TagDTO> findAll(int page, int size) throws ServiceException, IncorrectArgumentException {
        try {
            long count = count();
            Page tagPage = new Page(page, size, count);
            List<Tag> tags = tagDao.findAll(tagPage.getOffset(), tagPage.getLimit());
            return tags.stream().map(dtoMapper::toDTO).collect(Collectors.toList());
        } catch (DataAccessException e) {
            log.error("Find all tag service exception", e);
            throw new ServiceException("Find all tag service exception", e);
        }
    }

    @Override
    public long count() throws ServiceException {
        try {
            return tagDao.getCountOfEntities();
        } catch (DataAccessException e) {
            log.error("Count tag service exception", e);
            throw new ServiceException("Count tag service exception", e);
        }
    }
}
