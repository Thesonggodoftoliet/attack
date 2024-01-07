package com.littlepants.attack.attackplus.service;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 *
 * </p>
 *
 * @author 厕所歌神李狗蛋
 * @version JAVA17
 * @since 2023/4/27
 */

public interface BaseNeo4jService<E,ID> {
    List<E> getAll();
    Optional<E> getOneById(ID id);
    E createOrUpdate(E entity);
    void deleteById(ID id);
}
