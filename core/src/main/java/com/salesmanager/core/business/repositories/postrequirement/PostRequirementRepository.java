package com.salesmanager.core.business.repositories.postrequirement;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.salesmanager.core.model.postrequirement.PostRequirement;

public interface PostRequirementRepository extends JpaRepository<PostRequirement,Long>, PostRequirementRepositoryCustom {

	@Query("select pr from PostRequirement pr order by pr.postedDate desc")
	List<PostRequirement> findAllPostRequirements();

	@Query("select pr from PostRequirement pr where pr.customerId = ?1 order by pr.postedDate desc")
	List<PostRequirement> findPostRequirementsByCustomerId(Long customerId);

	@Query(value="SELECT * FROM POST_REQUIREMENT pr where DATE(pr.POST_DATE) between ?1 and ?2 order by pr.POST_DATE desc", nativeQuery=true)
	List<PostRequirement> getAllPostRequirements(Date startDate, Date endDate);

	//@Query("select pr from PostRequirement pr where pr.state = ?1 and date(pr.postedDate) between ?2 and ?3 order by pr.postedDate desc")
	@Query(value="SELECT * FROM POST_REQUIREMENT pr where pr.state = ?1 and " 
			+" DATE(pr.POST_DATE) between ?2 and ?3 order by pr.POST_DATE desc", nativeQuery=true)
	List<PostRequirement> getPostRequirementsBasedOnStatusAndDate(String status, Date startDate, Date endDate);
}
