package com.zdoryk.data.info.contact;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {

    @Query(value = "select c from contacts c where c.contactId=1")
    Contact getFirstContact();
}
