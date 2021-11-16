package com.scrape.brick.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import com.scrape.brick.model.Product;


// Tokopedia mobile phones 
// https://www.tokopedia.com/p/handphone-tablet/handphone

// https://www.youtube.com/watch?v=1a7esDg4JQY
// https://stackoverflow.com/questions/3666007/how-to-serialize-object-to-csv-file/3666052

// https://stackoverflow.com/questions/67741964/how-to-write-list-of-objects-in-a-csv-using-opencsv-csvwriter-in-java
@RestController
public class BrickController {

    private static final String baseUrl= "https://www.tokopedia.com/p/handphone-tablet/handphone?page=";
    private static final int pageCount = 10;

    @GetMapping("/scrape")
    public String getProducts(){
        WebClient client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
        client.getOptions().setUseInsecureSSL(true);
        String xpathExpr = "";
        List<Product> products = new ArrayList<Product>();
        try {
            int i = 1;
            while (products.size()<100){
                String urlPage = baseUrl+""+String.valueOf(i);
                HtmlPage page = client.getPage(urlPage);
                List<HtmlElement> items = page.getByXPath(xpathExpr="//div[contains(@class, 'css-bk6tzz e1nlzfl3')]") ;
                if (items.isEmpty()){
                    System.out.println("No items found");
                } else {
                    String productUrl = "";
                    for (HtmlElement htmlItem: items) {
                        Product topProduct = new Product();
                        HtmlAnchor itemAnchor = ((HtmlAnchor) htmlItem.getFirstByXPath(xpathExpr=".//a[contains(@class, 'css-89jnbj')]") );
                        productUrl = itemAnchor.getHrefAttribute();
                        
                        HtmlElement titleElement = ((HtmlElement) htmlItem.getFirstByXPath(xpathExpr=".//span[@class='css-1bjwylw']") );
                        HtmlElement shopElement = ((HtmlElement) htmlItem.getByXPath(xpathExpr=".//span[@class='css-1kr22w3']").get(1));
                        // rating
                        HtmlDivision ratingDiv =  ((HtmlDivision) htmlItem.getFirstByXPath(xpathExpr=".//div[@class='css-153qjw7']") );
                        int rating =0;
                        if(ratingDiv != null ){
                            List<HtmlElement> imgs = ratingDiv.getByXPath(xpathExpr=".//img[@class='css-177n1u3']");
                            rating = imgs.size();
                        }
                        // Price
                        HtmlDivision priceElement =  ((HtmlDivision) htmlItem.getFirstByXPath(xpathExpr=".//div[@class='css-4u82jy']") );
                        if (rating == 5 && products.size()<100) {
                            topProduct.setImageLink(productUrl);
                            topProduct.setPrice(priceElement.asNormalizedText());
                            topProduct.setName(titleElement.asNormalizedText());
                            topProduct.setStoreName(shopElement.asNormalizedText());
                            topProduct.setRating(rating);
                            products.add(topProduct);
                        }
                        

                    }
                
                }
            i++;
            }
            System.out.println(" ALL PRODUCTS ARE "+products.size());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            writeToCsv(products, "name,imageLink,price,rating,store");
        } catch (IOException e) {
            System.out.println("Error while writing to CSV");
            e.printStackTrace();
        }
		return "CSV Created successfully";
        
    }

    public void writeToCsv(List<Product> sampleList, String header) throws IOException {
        PrintWriter writer = new PrintWriter("products.csv");
        writer.println(header);

        for (Product topProduct : sampleList) {
            String line = topProduct.getName()+','+ topProduct.getImageLink()+','+
                          topProduct.getPrice()+',' + String.valueOf(topProduct.getRating())+
                          ','+ topProduct.getStoreName();
            writer.println(line);
        }
        writer.close();
    }
}

    

