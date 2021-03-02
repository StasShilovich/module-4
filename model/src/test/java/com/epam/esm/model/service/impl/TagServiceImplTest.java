package com.epam.esm.model.service.impl;

import com.epam.esm.model.dao.TagDao;
import com.epam.esm.model.dao.entity.Tag;
import com.epam.esm.model.service.TagService;
import com.epam.esm.model.service.converter.impl.TagDTOMapper;
import com.epam.esm.model.service.dto.TagDTO;
import com.epam.esm.model.service.exception.IncorrectArgumentException;
import com.epam.esm.model.service.exception.ServiceException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class TagServiceImplTest {

    @Mock
    TagDao dao;
    TagDTOMapper tagConverter;
    TagService service;
    Tag tag;
    TagDTO tagDTO;

    @BeforeEach
    public void setUp() {
        tagConverter = new TagDTOMapper();
        service = new TagServiceImpl(dao, tagConverter);
        tag = new Tag(1L, "name");
        tagDTO = new TagDTO(1L, "name");
    }

    @Test
    void testFindTagPositive() throws ServiceException {
        lenient().when(dao.findById(anyLong())).thenReturn(Optional.of(tag));
        TagDTO actual = service.find(1L);
        assertEquals(tagDTO, actual);
    }

    @Test
    void testFindTagNegative() throws ServiceException {
        lenient().when(dao.findById(anyLong())).thenReturn(Optional.of(tag));
        TagDTO actual = service.find(1L);
        assertNotEquals(null, actual);
    }

    @Test
    void testFindTagServiceException() {
        lenient().when(dao.findById(anyLong())).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.find(1L));
    }

    @Test
    void testAddPositive() throws ServiceException {
        lenient().when(dao.create(any(Tag.class))).thenReturn(tag);
        TagDTO actual = service.add(tagDTO);
        assertEquals(tagDTO, actual);
    }

    @Test
    void testAddNegative() throws ServiceException {
        lenient().when(dao.create(any(Tag.class))).thenReturn(tag);
        TagDTO actual = service.add(tagDTO);
        assertNotEquals(new TagDTO(), actual);
    }

    @Test
    void testAddServiceException() {
        lenient().when(dao.create(any(Tag.class))).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.add(tagDTO));
    }

    @Test
    void testFindAllPositive() throws ServiceException, IncorrectArgumentException {
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        lenient().when(dao.findAll(anyInt(), anyInt())).thenReturn(tags);
        List<TagDTO> actual = service.findAll(1, 3);
        List<TagDTO> expected = new ArrayList<>();
        expected.add(tagDTO);
        assertEquals(expected, actual);
    }

    @Test
    void testFindAllNegative() throws ServiceException, IncorrectArgumentException {
        List<Tag> tags = new ArrayList<>();
        tags.add(tag);
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        lenient().when(dao.findAll(anyInt(), anyInt())).thenReturn(tags);
        List<TagDTO> actual = service.findAll(1, 3);
        assertNotEquals(0, actual.size());
    }

    @Test
    void testFindAllServiceException() {
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        lenient().when(dao.findAll(anyInt(), anyInt())).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.findAll(1, 3));
    }

    @Test
    void testFindAllIncorrectArgumentException() {
        lenient().when(dao.getCountOfEntities()).thenReturn(9L);
        assertThrows(IncorrectArgumentException.class, () -> service.findAll(4, 3));
    }

    @Test
    void testCountPositive() throws ServiceException {
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        long actual = service.count();
        assertEquals(10L, actual);
    }

    @Test
    void testCountNegative() throws ServiceException {
        lenient().when(dao.getCountOfEntities()).thenReturn(10L);
        long actual = service.count();
        assertNotEquals(1L, actual);
    }

    @Test
    void testCountServiceException() {
        lenient().when(dao.getCountOfEntities()).thenThrow(EmptyResultDataAccessException.class);
        assertThrows(ServiceException.class, () -> service.count());
    }
}
