package org.mpaa;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

/* For more information on the meanings of the MPAA ratings, see
 * http://en.wikipedia.org/wiki/Motion_Picture_Association_of_America_film_rating_system#From_M_to_PG
 */
@XmlEnum(String.class)
public enum Rating {

   G,
   PG,
   @XmlEnumValue("PG-13")
   PG_13("PG-13"),
   R,
   @XmlEnumValue("NC-17")
   NC_17("NC-17"),
   X, // X was replaced in 1990 by NC-17
   GP, // GP was replaced in 1972 by PG
   M, // M was replaced in 1970 by GP
   @XmlEnumValue("M/PG")
   M_PG("M/PG");

   private String code;

   Rating() {};

   Rating(String code) {
      this.code = code;
   }

   @Override
   public String toString() {
      return code != null ? code : this.name();
   }

   public static Rating parse(String value) {
      if (value == null) throw new NullPointerException();
      for (Rating rating : Rating.values())  {
         if (rating.toString().equals(value)) {
            return rating;
         }
      }
      throw new IllegalArgumentException(value + " is not a valid Rating.");
   }
}
