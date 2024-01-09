package com.fw.channel.catalog.repo;

import com.fw.model.entity.cl.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    List<Catalog> findByLineAndType(@Param("line") String line, @Param("type") String type);

    List<Catalog> findByLineAndTypeAndCode(@Param("line") String line, @Param("type") String type, @Param("code") String code);

    List<Catalog> findAll();

    Optional<Catalog> findById(Long id);

    List<Catalog> findByParentid(Long id);

    Catalog findByTypeAndCode(@Param("type") String type, @Param("code") String code);

    @Query(value = "SELECT DISTINCT  c.ID, c.LINE, c.TYPE, c.CODE, c.LABEL,c.DESCRIPTION,c.WEIGHT, c.PARENT_ID, c.LABEL_CODE " +
            "FROM CATALOG c " +
            "INNER JOIN CRD_CONF_BUSINESS_TYPE crd " +
            "ON c.LABEL_CODE = crd.TRANS_TYPE " +
            "WHERE  c.LINE = 'VHTD' AND c.CODE = 'TRANS_TYPE' AND crd.IS_ACTIVE = 1", nativeQuery = true)
    List<Catalog> findCatalogWithType();

    @Query(value = "select distinct c.ID, c.LINE, c.TYPE, c.CODE, c.LABEL,c.DESCRIPTION,c.WEIGHT, c.PARENT_ID, c.LABEL_CODE " +
            "from catalog c inner join CRD_CONF_BUSINESS_TYPE crd " +
            "on c.LABEL_CODE = crd.TRANS_DETAIL where  c.LINE = 'VHTD' and c.CODE = 'TRANS_DETAIL' and crd.IS_ACTIVE = 1", nativeQuery = true)
    List<Catalog> findCatalogWithDetail();

    @Query(value = "SELECT DISTINCT C.ID,C.LINE, C.TYPE, C.CODE,C.LABEL,C.DESCRIPTION,C.WEIGHT, C.PARENT_ID,C.LABEL_CODE " +
            "FROM CATALOG C " +
            "INNER JOIN TFT_TRANS_CATALOG TS " +
            "ON C.LABEL_CODE = TS.SUB_TRANS_CODE " +
            "WHERE C.LINE = 'TT_TTTM' AND C.CODE = 'TRANS_CHILD' AND TS.IS_ON = 1 ", nativeQuery = true)
    List<Catalog> findOnOfLineTTTM();

}
