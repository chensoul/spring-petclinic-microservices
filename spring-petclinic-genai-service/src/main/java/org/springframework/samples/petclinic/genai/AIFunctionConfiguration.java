package org.springframework.samples.petclinic.genai;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import java.util.function.Function;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.samples.petclinic.genai.dto.OwnerDetails;
import org.springframework.samples.petclinic.genai.dto.PetDetails;
import org.springframework.samples.petclinic.genai.dto.PetRequest;
import org.springframework.samples.petclinic.genai.dto.Vet;

/**
 * This class defines the @Bean functions that the LLM provider will invoke when it
 * requires more Information on a given topic. The currently available functions enable
 * the LLM to get the list of owners and their pets, get information about the
 * veterinarians, and add a pet to an owner.
 *
 * @author Oded Shopen
 */
@Configuration
class AIFunctionConfiguration {

	// The @Description annotation helps the model understand when to call the function
	@Bean
	@Description("List the owners that the pet clinic has")
	public Function<OwnerRequest, OwnersResponse> listOwners(AIDataProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.getAllOwners();
		};
	}

	@Bean
	@Description("Add a new pet owner to the pet clinic. " + "The Owner must include a first name and a last name "
			+ "as two separate words, " + "plus an address and a 10-digit phone number")
	public Function<OwnerRequest, OwnerResponse> addOwnerToPetclinic(AIDataProvider petclinicAiDataProvider) {
		return request -> {
			return petclinicAiDataProvider.addOwnerToPetclinic(request);
		};
	}

	@Bean
	@Description("List the veterinarians that the pet clinic has")
	public Function<VetRequest, VetResponse> listVets(AIDataProvider petclinicAiProvider) {
		return request -> {
			try {
				return petclinicAiProvider.getVets(request);
			}
			catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
		};
	}

	@Bean
	@Description("Add a pet with the specified petTypeId, " + "to an owner identified by the ownerId. "
			+ "The allowed Pet types IDs are only: " + "1 - cat" + "2 - dog" + "3 - lizard" + "4 - snake" + "5 - bird"
			+ "6 - hamster")
	public Function<AddPetRequest, AddedPetResponse> addPetToOwner(AIDataProvider petclinicAiProvider) {
		return request -> {
			return petclinicAiProvider.addPetToOwner(request);
		};
	}

}

record AddPetRequest(PetRequest pet, Integer ownerId) {
};

record OwnersResponse(List<OwnerDetails> owners) {
};

record OwnerResponse(OwnerDetails owner) {
};

record AddedPetResponse(PetDetails pet) {
};

record VetResponse(List<String> vet) {
};

record VetRequest(Vet vet) {

};

record OwnerRequest(@NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String address,
        @NotBlank String city,
        @NotBlank @Digits(fraction = 0, integer = 12) String telephone) {
};
