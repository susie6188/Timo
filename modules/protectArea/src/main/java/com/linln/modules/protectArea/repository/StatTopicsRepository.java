package com.linln.modules.protectArea.repository;

import com.linln.modules.protectArea.domain.ITopicTO;
import com.linln.modules.protectArea.domain.StatTopics;
import com.linln.modules.system.repository.BaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author susie
 * @date 2021/01/18
 */
public interface StatTopicsRepository extends BaseRepository<StatTopics, Long>,
                                              JpaSpecificationExecutor<StatTopics> {

    @Query(nativeQuery = true, value = "select distinct topics as name from stat_stat_topics")
    public List<ITopicTO> findTopics();

    @Query(nativeQuery = true, value = "select distinct sub_topics as name from stat_stat_topics where topics = :topic")
    public List<ITopicTO> findSubTopics(@Param("topic") String topic);
}