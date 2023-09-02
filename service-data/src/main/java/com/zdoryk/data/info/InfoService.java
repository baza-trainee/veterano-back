package com.zdoryk.data.info;

import com.zdoryk.data.dto.ContactDTO;
import com.zdoryk.data.dto.PartnerDTO;
import com.zdoryk.data.dto.PartnersPagination;
import com.zdoryk.data.exception.NotFoundException;
import com.zdoryk.data.exception.NotValidFieldException;
import com.zdoryk.data.image.Image;
import com.zdoryk.data.image.ImageService;
import com.zdoryk.data.info.contact.Contact;
import com.zdoryk.data.info.contact.ContactRepository;
import com.zdoryk.data.info.partner.Partner;
import com.zdoryk.data.info.partner.PartnerRepository;
import com.zdoryk.data.mappers.PartnerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InfoService {

    private final PartnerRepository partnerRepository;
    private final ContactRepository contactRepository;
    private final ImageService imageService;
    private final PartnerMapper partnerMapper;
    @Transactional
    @CacheEvict(cacheNames = "partners", allEntries = true)
    public void savePartner(PartnerDTO partnerToSave){

        Image image =  Image.builder()
                .image(partnerToSave.image())
                .build();

        Partner partner = Partner
                .builder()
                .partnerName(partnerToSave.partnerName())
                .url(partnerToSave.url())
                .image(image)
                .isEnabled(partnerToSave.isEnabled())
                .publication(partnerToSave.publication())
                .build();

        imageService.saveImage(image);
        partnerRepository.save(partner);
    }

    @Cacheable(cacheNames = "partners")
    public PartnersPagination getAllPartners(int pageNumber, int pageSize){

        List<PartnerDTO> partnerDTOList = partnerRepository.findAll()
                .stream()
                .map(partnerMapper::toPartnerDTO)
                .toList();


        int totalItems = partnerDTOList.size();
        int totalPages = (int) Math.ceil((double) totalItems / pageSize);

        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalItems);

        return new PartnersPagination(
                partnerDTOList.subList(startIndex,endIndex),
                totalPages,
                totalItems
        );
    }

    @CacheEvict(cacheNames = "partners", allEntries = true)
    public void deletePartner(Long id){
        Partner partner = partnerRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Partner does not exist"));

        Image image = partner.getImage();

        partnerRepository.delete(partner);
        imageService.deleteImage(image);
    }


    @CacheEvict(cacheNames = "contact", allEntries = true)
    public void updateContact(ContactDTO contactDTO){
        Contact contact = contactRepository.getFirstContact();
        if(contactDTO.firstPhoneNumber() != null && !contactDTO.firstPhoneNumber().isEmpty()){
            contact.setFirstPhoneNumber(contactDTO.firstPhoneNumber());
        }
        if(contactDTO.secondPhoneNumber() != null && !contactDTO.secondPhoneNumber().isEmpty()){
            contact.setSecondPhoneNumber(contactDTO.secondPhoneNumber());
        }
        if(contactDTO.email() != null && !contactDTO.email().isEmpty()){
            contact.setEmail(contactDTO.email());
        }
        contactRepository.save(contact);
    }

    @Cacheable(cacheNames = "contact")
    public Contact getContact(){
        return contactRepository.getFirstContact();
    }

    @Transactional
//    @Scheduled(fixedRate = 12 * 60 * 60 * 1000)
    @Scheduled(fixedRate = 10000L)
    public void enablePartners(){

        List<Partner> partners = partnerRepository
                .getPartnersByIsEnabledFalse()
                .stream()
                .filter(partner ->
                        partner.getPublication().isEqual(LocalDate.now()) ||
                        partner.getPublication().isBefore(LocalDate.now()))
                .toList();

        if(!partners.isEmpty()){
            partners.forEach(card -> card.setIsEnabled(true));
            partnerRepository.saveAll(partners);
        }
    }

    @Transactional
    public void updatePartner(PartnerDTO partnerDTO) {
        if(partnerDTO.id() == null){
            throw new NotValidFieldException("Id should exist");
        }
        Partner partner = partnerRepository.getPartnerByPartnerId(partnerDTO.id())
                .orElseThrow(() -> new NotFoundException("Partner does not exist"));

        if(partnerDTO.url() != null && !partnerDTO.url().isEmpty()){
            partner.setUrl(partnerDTO.url());
        }
        if(partnerDTO.image() != null && !partnerDTO.image().isEmpty()){
            Image image = partner.getImage();
            image.setImage(partnerDTO.image());
            imageService.saveImage(image);
        }
        if(partnerDTO.partnerName() != null && !partnerDTO.partnerName().isEmpty()){
            partner.setPartnerName(partnerDTO.partnerName());
        }
        if(partnerDTO.publication() != null){
            partner.setPublication(partnerDTO.publication());
        }
        if(partnerDTO.isEnabled() != null){
            partner.setIsEnabled(partnerDTO.isEnabled());
        }
        partnerRepository.save(partner);
    }

    public PartnerDTO getPartnerById(Long id){
        Partner partner = partnerRepository.getPartnerByPartnerId(id)
                .orElseThrow(() -> new NotFoundException("Partner does not exist"));
        return partnerMapper.toPartnerDTO(partner);
    }

}
