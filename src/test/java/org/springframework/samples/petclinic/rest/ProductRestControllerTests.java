/*
 * Copyright 2016-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.model.Product;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.samples.petclinic.service.clinicService.ApplicationTestConfig;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


/**
 * Test class for {@link ProductRestController}
 *
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=ApplicationTestConfig.class)
@WebAppConfiguration
@WithMockUser(roles="PRODUCT_ADMIN")
public class ProductRestControllerTests {

    private static final int TEST_PRODUCT_ID = 1;
    
    @Autowired
    private ProductRestController productRestController;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<Product> products;
    private Product golden;

    @Before
    public void initProducts(){
    	this.mockMvc = MockMvcBuilders.standaloneSetup(productRestController)
    			.setControllerAdvice(new ExceptionControllerAdvice())
    			.build();
    	products = new ArrayList<Product>();
    	
		golden = new Product();
		golden.setId(TEST_PRODUCT_ID);
		golden.setName("Golden gato adulto salmao");
		golden.setDescription("Formulada com ingredientes de alta qualidade e balanceada para suprir às particularidades nutricionais dos felinos, Golden Gatos Adultos Salmão oferece sabor irresistível  e cuidados especiais com o trato urinário, garantindo assim, sabor e saúde para seu gato.");
		
		products.add(golden);
 
    }

    @Test
    public void testGetAllProductsSuccess() throws Exception {
    	given(this.clinicService.findAllProducts()).willReturn(products);
        this.mockMvc.perform(get("/api/products/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(jsonPath("$.[0].id").value(TEST_PRODUCT_ID));
//            .andExpect(jsonPath("$.[0].firstName").value("Betty"))
//            .andExpect(jsonPath("$.[1].id").value(4))
//            .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }

    @Test
    public void testGetAllProductsNotFound() throws Exception {
    	products.clear();
    	given(this.clinicService.findAllProducts()).willReturn(products);
        this.mockMvc.perform(get("/api/products/")
        	.accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    
}
