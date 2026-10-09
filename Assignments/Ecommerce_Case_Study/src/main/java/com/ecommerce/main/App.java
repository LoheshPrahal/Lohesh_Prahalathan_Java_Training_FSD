package com.ecommerce.main;

import com.ecommerce.config.AppConfig;
import com.ecommerce.dto.ProductDto;
import com.ecommerce.enums.CategoryName;
import com.ecommerce.enums.VendorName;
import com.ecommerce.model.Product;
import com.ecommerce.service.CategoryService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.VendorService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class App {
    public static void main(String[] args){
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        ProductService productService = context.getBean(ProductService.class);
        CategoryService categoryService = context.getBean(CategoryService.class);
        VendorService vendorService = context.getBean(VendorService.class);

        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.println("----------------Ecommerce App--------------------");
            System.out.println("1. Add Product");
            System.out.println("2. Find Product By ID");
            System.out.println("3. Update Stock Quantity");
            System.out.println("4. Count product by vendor");
            System.out.println("-------------------------------------------------");

            int input = sc.nextInt();
            sc.nextLine();

            if(input == 0){
                System.out.println("Exiting Application.....");
                break;
            }

            switch (input){
                case 1 -> {
                    Product product = new Product();
                    System.out.println("Enter Product Name: ");
                    product.setName(sc.nextLine());

                    System.out.println("Enter Product Price: ");
                    product.setPrice(sc.nextDouble());

                    System.out.println("Enter Stock Quantity: ");
                    product.setStockQuantity(sc.nextInt());
                    sc.nextLine();

                    System.out.println("Enter Category name: ");
                    Arrays.stream(CategoryName.values()).forEach(System.out::println);

                    product.setCategory(categoryService.getCategory(sc.nextLine()));

                    System.out.println("Enter Vendor name: ");
                    Arrays.stream(VendorName.values()).forEach(System.out::println);
                    product.setVendor(vendorService.getVendor(sc.nextLine()));
                    try{
                        productService.insertProduct(product);
                        System.out.println("Product added successfully!!!");
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    break;
                }

                case 2 -> {
                    System.out.println("Enter Product ID to retrieve: ");
                    ProductDto product = productService.getProductById(sc.nextInt());
                    System.out.println(product);
                    break;
                }

                case 3 -> {
                    System.out.println("Enter Product Id to update stock quantity: ");
                    int id = sc.nextInt();
                    System.out.println("Enter new stock quantity: ");
                    int newStockQuantity = sc.nextInt();
                    productService.updateStockQuantity(id, newStockQuantity);
                    System.out.println("New Stock Quantity updated successfully!!!");
                    break;
                }

                case 4 -> {
                    System.out.println("Product count by each vendor: ");
                    Map<String, Integer> map = vendorService.countProductsByVendor();
                    map.forEach((key, value) -> System.out.println(key + "->" + value));
                }
            }
        }
    }
}
