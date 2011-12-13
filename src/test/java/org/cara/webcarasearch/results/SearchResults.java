package org.cara.webcarasearch.results;

import com.trickl.model.xml.adapter.XmlURLAdapter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.mpaa.Rating;

@XmlType(name = "SearchResults")
@XmlRootElement(name = "SearchResults")
public class SearchResults {

   @XmlType(name = "title")
   public static class Title {
      private String name;
      
      private int year;

      private URL url;

      private Rating rating;

      private String ratingReason;

      private String distributor;

      private String other;
      
      private Rating prevRating;
      
      private int prevRatingYear;

      private String notes;

      @XmlElement
      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }

      @XmlElement
      public int getYear() {
         return year;
      }

      public void setYear(int year) {
         this.year = year;
      }

      @XmlElement
      @XmlJavaTypeAdapter(XmlURLAdapter.class)
      public URL getUrl() {
         return url;
      }

      public void setUrl(URL url) {
         this.url = url;
      }

      @XmlElement
      public Rating getRating() {
         return rating;
      }

      public void setRating(Rating rating) {
         this.rating = rating;
      }

      @XmlElement
      public String getRatingReason() {
         return ratingReason;
      }

      public void setRatingReason(String ratingReason) {
         this.ratingReason = ratingReason;
      }

      @XmlElement
      public String getDistributor() {
         return distributor;
      }

      public void setDistributor(String distributor) {
         this.distributor = distributor;
      }

      @XmlElement
      public String getOther() {
         return other;
      }

      public void setOther(String other) {
         this.other = other;
      }

      @XmlElement(name="prevrating")
      public Rating getPrevRating() {
         return prevRating;
      }

      public void setPrevRating(Rating prevRating) {
         this.prevRating = prevRating;
      }

      @XmlElement(name="prevratingyear")
      public int getPrevRatingYear() {
         return prevRatingYear;
      }

      public void setPrevRatingYear(int prevRatingYear) {
         this.prevRatingYear = prevRatingYear;
      }

      @XmlElement
      public String getNotes() {
         return notes;
      }

      public void setNotes(String notes) {
         this.notes = notes;
      }
   }

   private int totalResponse;

   private List<Title> titles = new ArrayList<Title>();

   @XmlElement(name = "total_response")
   public int getTotalResponse() {
      return totalResponse;
   }

   public void setTotalResponse(int totalResponse) {
      this.totalResponse = totalResponse;
   }

   @XmlElement(name="title")
   public List<Title> getTitles() {
      return titles;
   }

   public void setTitles(List<Title> titles) {
      this.titles = titles;
   }
}

