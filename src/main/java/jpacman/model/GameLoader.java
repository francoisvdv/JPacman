package jpacman.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Take care of loading a map of a game from a
 * resource file.
 * 
 * @author Arie van Deursen, Delft University of Technology, January 2009
 * @version $Id: GameLoader.java 5170 2012-01-18 08:37:54Z arievandeursen $
 */

public class GameLoader
{
    
    /**
     * The default map of the world, used if
     * no specific map file is provided, or if loading the
     * map file is going wrong.
     */
    static final String[] DEFAULT_WORLD_MAP = new String[] {
        "WWWWWWWWWWWWWWWWWWWW",
        "W000M0W00000M0000F0W",
        "W0F00MW000000000000W",
        "W0000000000M000F000W",
        "W000000000000F00000W",
        "W000MM0000M00000000W",
        "W000000M00000000000W",
        "W00M00000F0000000WWW",
        "W0000000000000WWW00W",
        "W000000000000000000W",
        "W0000WW0WWW00000000W",
        "W0000W00F0W00000000W",
        "W0P00W0M00W00000000W",
        "W0000WWWWWW00000F00W",
        "W000000000000000000W",
        "W000000000000000000W",
        "W0F0000000000F00M00W",
        "W0WWWW0000000000000W",
        "W00F00WWWW000000WWWW",
        "WWWWWWWWWWWWWWWWWWWW"
    };

    /**
     * Actually obtain a map from a file specified in the 
     * default property file.
     * @return The map in the file
     * @throws GameLoadException If the map is wrong or the files can't be opened.
     */
    public String[] obtainMap() throws GameLoadException
    {
        loadProperties(PROPERTY_FILE);
        String mapFile = getMapFileName(MAP_FILE_PROPERTY);
        String[] map = getMap(mapFile);
        String msg = checkSanity(map);
        if (msg != null)
        {
            throw new GameLoadException(msg);
        }
        return map;
    }
        
    /**
     * The name of the property file.
     */
    static final String PROPERTY_FILE = "jpacman.properties";

    /**
     * The  name of the property holding the name of the map file.
     */
    static final String MAP_FILE_PROPERTY = "jpacman.map.filename";
   
    /**
     * The default name for the map file to be loaded.
     */
    static final String DEFAULT_MAP_FILE = "level1.txt";
    
    /**
     * The properties for this game.
     */
    private Properties properties = null;
    
    
    /**
     * Load a propertyfile.
     * @param propertyFile The file to be loaded, not null.
     * @throws GameLoadException If the property file can't be read.
     */
    void loadProperties(String propertyFile) throws GameLoadException
    {
        assert propertyFile != null;
        properties = new Properties();
        InputStream propertyStream = getResourceStream(propertyFile);
        assert propertyStream != null : "Otherwise exception";
        try
        {
            properties.load(propertyStream);
        } catch (IOException e)
        {
            throw new GameLoadException("Can't load " + propertyFile, e);
        }
    }

    /**
     * Obtain the name of the map file from the property file,
     * which is assumed to be read already (using loadProperties()).
     * @param mapFileProperty Property name for the map file
     * @return The name of the map file.
     * @throws GameLoadException If requested property doesn't exist
     */
    String getMapFileName(String mapFileProperty) throws GameLoadException
    {
        assert properties != null;
        String theMapFile = properties.getProperty(mapFileProperty);
        if (theMapFile == null)
        {
            throw new GameLoadException("Property " + mapFileProperty + " not defined.");
        }
        assert theMapFile != null;
        return theMapFile;
    }
    
    /**
     * Return the map contained in the file.
     * @param fileName Resource on classpath containing the map
     * @return The map as a series of strings.
     * @throws GameLoadException If reading the map file fails.
     */
    String[] getMap(String fileName) throws GameLoadException
    {
        assert fileName != null;
        List<String> mapList = new ArrayList<String>();
        String[] mapString = null;

        try
        {
            BufferedReader br = 
                new BufferedReader(new InputStreamReader(getResourceStream(fileName)));
            while (br.ready())
            {
                mapList.add(br.readLine());
            }
            mapString = new String[mapList.size()];
            mapList.toArray(mapString);
            br.close();
        } catch (IOException e)
        {
            throw new GameLoadException("Problem reading file " + fileName, e);
        }
        return mapString;
    }
    
    /**
     * Provide a stream for a file that lives on the class path.
     * @param fileName The name of the resource to be loaded
     * @return an input stream for the file, never null.
     * @throws GameLoadException If the file is not found.
     */
    private InputStream getResourceStream(String fileName) throws GameLoadException
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        InputStream result = cl.getResourceAsStream(fileName);
        if (result == null)
        {
            throw new GameLoadException("Resource: " + fileName + " not found on classpath:" 
                    + System.getProperty("java.class.path"));
        }
        assert result != null;
        return result;
    }
    
    /**
     * Check the correctness of a given map.
     * @param map The map to be checked.
     * @return null if the map is ok, an error message explaining the cause otherwise.
     */
    public static String checkSanity(String[] map)
    {
        assert map != null;
        
        int height = map.length;
        if (height == 0)
        {
            return "Empty board not allowed";
        }
        int width = map[0].length();
        if (width == 0)
        {
            return "Empty rows not permitted.";
        }
        
       boolean playerEncountered = false;
        
        for (int y = 0; y < height; y++)
        {
            if (map[y].length() != width)
            {
                return "all lines in map should be of equal length.";
            }
            for (int x = 0; x < width; x++)
            {
                char ch = map[y].charAt(x);
                if (!permittedChar(ch))
                {
                    return "Incorrect game character: " + ch;
                }
                if (ch == Guest.PLAYER_TYPE)
                {
                    playerEncountered = true;
                }
            }
       }
        if (!playerEncountered)
        {
            return "No player defined.";
        }
        
        return null; // = ok.
    }
    
    /**
     * Identify whether a character is permitted on a map.
     * @param c The char to be checked
     * @return true iff c is a valid char.
     */
    @SuppressWarnings("PMD.MissingBreakInSwitch")
    private static boolean permittedChar(char c)
    {
        switch(c)
        {
        case Guest.EMPTY_TYPE:
        case Guest.FOOD_TYPE:
        case Guest.MONSTER_TYPE:
        case Guest.PLAYER_TYPE:
        case Guest.WALL_TYPE:
            return true;
        default:
            return false;
        }     
    }    
}