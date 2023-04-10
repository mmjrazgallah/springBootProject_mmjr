package com.sip.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;


@Entity

public class Article {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "Label is mandatory")
    @Column(name = "label")
    private String label;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private float price;

    @Column(name = "priceSale")
    private float priceSale;

    @Column(name = "dateExpiration")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateExpiration;

    @Column(name = "picture")
    private String picture;

    @Column(name = "pictureFace")
    private String pictureFace;


    /**** Many To One ****/
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Provider provider;

    public Article() {}

    public Article(long id, String label, String description, float price, float priceSale,LocalDate dateExpiration) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.price = price;
        this.priceSale = priceSale;
        this.dateExpiration=dateExpiration;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

    public float getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(float priceSale) {
        this.priceSale = priceSale;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPictureFace() {
        return pictureFace;
    }

    public void setPictureFace(String pictureFace) {
        this.pictureFace = pictureFace;
    }

    public LocalDate getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(LocalDate dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public Provider getProvider() {
    	return provider;
    }
    
    public void setProvider(Provider provider) {
    	this.provider=provider;
    }  
    
}
