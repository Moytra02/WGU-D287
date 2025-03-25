package com.example.demo.bootstrap;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.repositories.OutsourcedPartRepository;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.service.OutsourcedPartService;
import com.example.demo.service.OutsourcedPartServiceImpl;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 *
 *
 *
 *
 */
@Component
public class BootStrapData implements CommandLineRunner {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;

    private final OutsourcedPartRepository outsourcedPartRepository;

    public BootStrapData(PartRepository partRepository, ProductRepository productRepository, OutsourcedPartRepository outsourcedPartRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
        this.outsourcedPartRepository = outsourcedPartRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        List<OutsourcedPart> outsourcedParts=(List<OutsourcedPart>) outsourcedPartRepository.findAll();
        for(OutsourcedPart part:outsourcedParts){
            System.out.println(part.getName()+" "+part.getCompanyName());
        }
        if (partRepository.count() == 0) {
            InhousePart hardware = new InhousePart();
            hardware.setName("Hardware");
            hardware.setPrice(19.99);
            hardware.setInv(10);

            InhousePart gripTape = new InhousePart();
            gripTape.setName("Grip Tape");
            gripTape.setPrice(29.99);
            gripTape.setInv(10);

            InhousePart wheels = new InhousePart();
            wheels.setName("Wheels");
            wheels.setPrice(39.99);
            wheels.setInv(10);

            InhousePart trucks = new InhousePart();
            trucks.setName("Trucks");
            trucks.setPrice(49.99);
            trucks.setInv(10);

            InhousePart bearings = new InhousePart();
            bearings.setName("Bearings");
            bearings.setPrice(59.99);
            bearings.setInv(10);

            partRepository.save(hardware);
            partRepository.save(gripTape);
            partRepository.save(wheels);
            partRepository.save(trucks);
            partRepository.save(bearings);
        }

        if (productRepository.count() == 0 ) {
            Product skateBoard = new Product("Skate Board", 199.99, 15);
            Product LongBoard = new Product("Long Board", 199.99, 15);
            Product cruiserBoard = new Product("Cruiser Board", 199.99, 15);
            Product pennyBoard = new Product("Penny Board", 99.99, 15);
            Product eBoard = new Product("Electric Board", 299.99, 15);

            productRepository.save(skateBoard);
            productRepository.save(LongBoard);
            productRepository.save(cruiserBoard);
            productRepository.save(pennyBoard);
            productRepository.save(eBoard);
        }

        System.out.println("Started in Bootstrap");
        System.out.println("Number of Products"+productRepository.count());
        System.out.println(productRepository.findAll());
        System.out.println("Number of Parts"+partRepository.count());
        System.out.println(partRepository.findAll());

    }
}