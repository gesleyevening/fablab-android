package de.fau.cs.mad.fablab.android.view.fragments.productmap;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class LocationParser
{
    protected static String testString = "FAU FabLab / Elektrowerkstatt / Regal / Kiste Dioden";

    private LocationParser(){   }


    public static ProductLocation getLocation(String locationString)
    {
        ProductLocation productLocation = null;

        if(locationString != null)
        {
            String splittedString[] = splitLocationString(locationString);
            String lastPartOfLocationString = splittedString[splittedString.length-1];

            if(hasIdentificationCode(lastPartOfLocationString))
            {
                String identificationCode = extractIdentificationCode(lastPartOfLocationString);
                productLocation = convertIdenticationCodeToProductLocation(identificationCode);
            }
            else
            {
                productLocation = convertLocationStringToLocation(splittedString);
            }

        }

        return productLocation;
    }

    // first filter
    private static String[] splitLocationString(String locationString)
    {
        String[] parts = locationString.split(" / ");
        return parts;
    }

    protected static String identificationCodeRegex = "\\([A-Z]\\d*(\\.\\d)?\\)";  // ...Regal (K) oder ...Halter (E6.1) oder .. (D1) oder ... (W10)
    protected static Pattern identificationCodePattern = Pattern.compile(identificationCodeRegex);

    private static boolean hasIdentificationCode(String lastPartOfString)
    {
        Matcher identificationCodeMatcher = identificationCodePattern.matcher(lastPartOfString);
        return identificationCodeMatcher.find();
    }

    // second filter
    private static String extractIdentificationCode(String lastPartOfString)
    {
        String result = "";
        Matcher identificationCodeMatcher = identificationCodePattern.matcher(lastPartOfString);
        if (identificationCodeMatcher.find())
        {
            result = identificationCodeMatcher.group();
        }
        return result;
    }

    // third filter
    private static ProductLocation convertIdenticationCodeToProductLocation(String identificationCode)
    {
        ProductLocation productLocation = null;
        for (ProductLocation loc : ProductLocation.values())
        {
            if(identificationCode.equals(loc.getIdentificationCode()))
            {
                productLocation = loc;
            }
        }
        try
        {
            if (productLocation == null)
                throw new IllegalArgumentException("Identification code: " + identificationCode + " is not available in ProductLocation.");
            else if(productLocation.getMainPositionX() == 0 && productLocation.getMainPositionY() == 0)
                throw new IllegalArgumentException("Identification code: " + identificationCode + " has no available position");
        }
        catch ( IllegalArgumentException ex)
        {
            System.out.println(ex.getMessage());
            productLocation = null;
        }
        finally
        {
            return productLocation;
        }

    }


    private static ProductLocation convertLocationStringToLocation(String[] splittedLocationString)
    {
        ProductLocation productLocation = null;

        for (ProductLocation loc : ProductLocation.values())
        {
            if (splittedLocationString[splittedLocationString.length - 1].equals(loc.getLocationName()))
            {
                productLocation = loc;
            }
        }

        // maybe the second last location name is known
        if (productLocation == null && splittedLocationString.length > 2)
        {
            for (ProductLocation loc : ProductLocation.values())
            {
                if (splittedLocationString[splittedLocationString.length - 2].equals(loc.getLocationName()))
                {
                    productLocation = loc;
                }
            }
        }
        try
        {
            if (productLocation == null)
                throw new IllegalArgumentException("location path " + splittedLocationString.toString() + " is not available in ProductLocation");
            else if(productLocation.getMainPositionX() == 0 && productLocation.getMainPositionY() == 0)
                throw new IllegalArgumentException("location path: " + splittedLocationString.toString() + " has no available position");
        }
        catch ( IllegalArgumentException ex)
        {
            System.out.println(ex.getMessage());
            productLocation = null;
        }
        finally
        {
            return  productLocation;
        }
    }

}