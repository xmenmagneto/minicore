package com.apple.minicore;

import com.apple.minicore.model.Owner;
import com.apple.minicore.repository.ComponentRepository;
import com.apple.minicore.repository.OwnerRepository;
import com.apple.minicore.test.TestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MinicoreApplication implements CommandLineRunner {


	@Autowired
	private ComponentRepository componentRepository;

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private TestData testData;


	public static void main(String[] args) {
		SpringApplication.run(MinicoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		componentRepository.deleteAllInBatch();
		ownerRepository.deleteAllInBatch();
		testData.createOwners();
	}


}

