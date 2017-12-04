package com.salesmanager.core.business.repositories.postrequirement;

import org.springframework.data.jpa.repository.JpaRepository;

import com.salesmanager.core.model.postrequirement.PostRequirement;

public interface PostRequirementRepository extends JpaRepository<PostRequirement,Long>, PostRequirementRepositoryCustom {

}
