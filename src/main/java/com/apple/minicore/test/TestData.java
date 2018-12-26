package com.apple.minicore.test;

import com.apple.minicore.model.Owner;
import com.apple.minicore.repository.ComponentRepository;
import com.apple.minicore.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Component
public class TestData {

    @Autowired
    private ComponentRepository componentRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    public void createOwners() {
        // create 10 owners and store into owners table
        Owner owner1 = new Owner(1, "Columbus Spinka", "spinka@apple.com");
        Owner owner2 = new Owner(2, "Stan Russel", "stan@apple.com");
        Owner owner3 = new Owner(3, "Donato Wehner", "donato@apple.com");
        Owner owner4 = new Owner(4, "Mireille Schaefer", "mireille@apple.com");
        Owner owner5 = new Owner(5, "Mathias Pfeffer", "mathias@apple.com");
        Owner owner6 = new Owner(6, "Gregg Brakus", "gregg@apple.com");
        Owner owner7 = new Owner(7, "Deshaun Nikolaus", "deshaun@apple.com");
        Owner owner8 = new Owner(8, "Arden Maggio", "arden@apple.com");
        Owner owner9 = new Owner(9, "Wilton Connelly", "wilton@apple.com");
        Owner owner10 = new Owner(10, "Alvena Hane", "alvena@apple.com");

        ownerRepository.save(owner1);
        ownerRepository.save(owner2);
        ownerRepository.save(owner3);
        ownerRepository.save(owner4);
        ownerRepository.save(owner5);
        ownerRepository.save(owner6);
        ownerRepository.save(owner7);
        ownerRepository.save(owner8);
        ownerRepository.save(owner9);
        ownerRepository.save(owner10);
    }
}
