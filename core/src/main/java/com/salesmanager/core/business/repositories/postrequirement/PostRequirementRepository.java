package com.salesmanager.core.business.repositories.postrequirement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.postrequirement.PostRequirement;

public interface PostRequirementRepository extends JpaRepository<PostRequirement,Long>, PostRequirementRepositoryCustom {

	@Query("select pr from PostRequirement pr order by pr.postedDate desc")
	List<PostRequirement> findAllPostRequirements();

	@Query("select pr from PostRequirement pr where pr.customerId = ?1 order by pr.postedDate desc")
	List<PostRequirement> findPostRequirementsByCustomerId(Long customerId);
}
