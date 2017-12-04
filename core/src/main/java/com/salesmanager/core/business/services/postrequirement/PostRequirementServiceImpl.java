package com.salesmanager.core.business.services.postrequirement;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.salesmanager.core.business.repositories.postrequirement.PostRequirementRepository;
import com.salesmanager.core.business.services.common.generic.SalesManagerEntityServiceImpl;
import com.salesmanager.core.model.postrequirement.PostRequirement;

@Service("postRequirementService")
public class PostRequirementServiceImpl extends SalesManagerEntityServiceImpl<Long,PostRequirement> implements PostRequirementService {

	@Inject
	PostRequirementRepository postRequirementRepository;
	
	@Inject
	PostRequirementServiceImpl(PostRequirementRepository postRequirementRepository) {
		super(postRequirementRepository);
		this.postRequirementRepository = postRequirementRepository;
	}

	@Override
	public List<PostRequirement> getAllPostRequirements() {
		// TODO Auto-generated method stub
		return postRequirementRepository.findAll();
	}
}