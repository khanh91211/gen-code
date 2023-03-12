package vn.gtel.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.gtel.model.entity.ConNguoi;

public interface ConNguoiRepository extends JpaRepository<ConNguoi, String> , JpaSpecificationExecutor<ConNguoi> {

    @Modifying
    @Query("Update ConNguoi ett set ett.status = :status where ett.id = :id")
    void approve(@Param("status") String status, @Param("id") String id);
}
