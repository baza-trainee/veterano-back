package com.zdoryk.data.info.partner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> getPartnersByIsEnabledFalse();

    Optional<Partner> getPartnerByPartnerId(Long id);


}
