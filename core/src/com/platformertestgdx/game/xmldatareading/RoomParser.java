package com.platformertestgdx.game.xmldatareading;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.platformertestgdx.game.block.Block;
import com.platformertestgdx.game.block.PassThroughBlock;
import com.platformertestgdx.game.block.SemiSolidBlock;
import com.platformertestgdx.game.block.SolidBlock;
import com.platformertestgdx.game.gameroom.GameRoom;

import java.util.ArrayList;

public class RoomParser {

    public static GameRoom getMapData(int mapID) {

        GameRoom room = new GameRoom();
        ArrayList<Block> blocks = new ArrayList<Block>();

        Block curBlock = new PassThroughBlock();

        try {

            Element root = new XmlReader().parse(Gdx.files.internal("Rooms/room"+ mapID +".xml"));

            Element properties = root.getChildByName("properties");


            int roomWidth = root.getIntAttribute("width",20);
            int roomHeight = root.getIntAttribute("height",10);

            room.setRoomDimensions(roomWidth, roomHeight);

            Array<Element> layers = root.getChildrenByName("layer");
            int[][] underLayer = new int[roomWidth][roomHeight];
            int[][] aboveLayer = new int[roomWidth][roomHeight];

            for (Element layer: layers) {
                boolean underL = layer.getAttribute("name").equalsIgnoreCase("Under Player");
                Element data = layer.getChildByName("data");
                Array<Element> tiles = data.getChildrenByName("tile");
                int count = 0;
                for (Element tile: tiles) {
                    if (underL)
                        underLayer[count%roomWidth][roomHeight-1-count/roomWidth] = tile.getIntAttribute("gid", 1);
                    else
                        aboveLayer[count%roomWidth][roomHeight-1-count/roomWidth] = tile.getIntAttribute("gid", 1);
                    count++;
                }
            }

            Array<Element> roomProperties = properties.getChildrenByName("property");

            for (Element property: roomProperties) {
                switch (whatProperty(property.getAttribute("name"))) {
                    case 1: // Room ID
                        room.setId(property.getInt("value", 0));
                        break;
                    case 2: // Room Name
                        room.setRoomName(property.getAttribute("value", "Error At Parsing"));
                        break;
                    case 3: // Background
                        room.setBackground(property.getAttribute("value", "bgnf"));
                        break;
                    case 4: // Tileset
                        room.setTileMap(property.getAttribute("value", "tsnf"), underLayer, aboveLayer);
                        break;
                    case 5: // X Position on the map
                        room.setGlobalX(property.getInt("value", 0));
                        break;
                    case 6: // Y Position on the map
                        room.setGlobalY(property.getInt("value", 0));
                        break;
                    case 7:
                        break;
                    default : break;
                }
            }


            Array<Element> objectGroups = root.getChildrenByName("objectgroup");

            for (Element objectGroup: objectGroups) {
                switch (whatObjectGroup(objectGroup.getAttribute("name"))) {
                    case 1: // Blocks

                        Array<Element> objects = objectGroup.getChildrenByName("object");

                        for (Element object: objects) {

                            switch (whatBlockType(object.getAttribute("type", "PassThrough"))) {
                                case 0: curBlock = new PassThroughBlock();
                                    break;
                                case 1: curBlock = new SolidBlock();
                                    break;
                                case 2: curBlock = new SemiSolidBlock();
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                            }

                            curBlock.setPosition(object.getInt("x", 0), object.getInt("y", 0));
                            curBlock.setDimensions(object.getInt("width", 32), object.getInt("height", 32));

                            properties = object.getChildByName("properties");

                            Array<Element> blockProperties;
                            if (properties != null) {
                                blockProperties = properties.getChildrenByName("property");

                                for (Element property: blockProperties) {

                                    switch (whatBlockProperty(property.getAttribute("name"))) {
                                        case 1: // Is a Slope
                                            ((SolidBlock)(curBlock)).setSlope(property.getBoolean("value", false));
                                            break;
                                        case 2: // Is Floor (true by default)
                                            ((SolidBlock)(curBlock)).setFloor(property.getBoolean("value", true));
                                            break;
                                        case 3: // Collision Key
                                            ((SolidBlock)(curBlock)).setCollisionKey(property.getAttribute("value", "PassThrough"));
                                            break;
                                        case 4: // Can Fade
                                            ((PassThroughBlock) curBlock).setCanFade(property.getBoolean("value", false));
                                            break;
                                        case 5: // Is In The Background
                                            ((PassThroughBlock) curBlock).setInBackground(property.getBoolean("value", false));
                                            break;
                                        case 6: //
                                            break;
                                        case 7: //
                                            break;
                                    }
                                }
                            }

                            curBlock.fixY(room.getRoomHeight()*32);
                            if (curBlock instanceof SolidBlock) ((SolidBlock)(curBlock)).createCollision();
                            blocks.add(curBlock);
                        }
                        room.setBlocks(blocks);
                        break;
                    case 2: // Door Blocks?
                        break;
                    default : break;
                }
            }




        } catch (Exception e) { e.printStackTrace();}

        return room;
    }

    private static int whatProperty(String propertyName) {
        int iProperty = 0;

        if      (propertyName.equalsIgnoreCase("roomID"))     iProperty = 1;
        else if (propertyName.equalsIgnoreCase("roomName"))   iProperty = 2;
        else if (propertyName.equalsIgnoreCase("background")) iProperty = 3;
        else if (propertyName.equalsIgnoreCase("tileset"))    iProperty = 4;
        else if (propertyName.equalsIgnoreCase("globalX"))    iProperty = 5;
        else if (propertyName.equalsIgnoreCase("globalY"))    iProperty = 6;
        //else if (propertyName.equalsIgnoreCase("roomID"))        iProperty = 2;

        return iProperty;
    }

    private static int whatObjectGroup(String objectGroupName) {
        int iObjectGroup = 0;

        if      (objectGroupName.equalsIgnoreCase("Blocks"))     iObjectGroup = 1;
        else if (objectGroupName.equalsIgnoreCase("Doors"))   iObjectGroup = 2;
        //else if (objectGroupName.equalsIgnoreCase("roomID"))        iObjectGroup = 3;

        return iObjectGroup;
    }

    private static int whatBlockType(String blockType) {
        int iBlockType = -1;

        if      (blockType.equalsIgnoreCase("PassThroughBlock")) iBlockType = 0;
        else if (blockType.equalsIgnoreCase("SolidBlock"))       iBlockType = 1;
        else if (blockType.equalsIgnoreCase("SemiSolidBlock"))   iBlockType = 2;
        else if (blockType.equalsIgnoreCase(""))            iBlockType = 3;

        return iBlockType;
    }

    private static int whatBlockProperty(String propertyName) {
        int iBlockProperty = 0;

        if      (propertyName.equalsIgnoreCase("isSlope"))        iBlockProperty = 1;
        else if (propertyName.equalsIgnoreCase("isFloor"))        iBlockProperty = 2;
        else if (propertyName.equalsIgnoreCase("collisionKey"))   iBlockProperty = 3;
        else if (propertyName.equalsIgnoreCase("canFade"))        iBlockProperty = 4;
        else if (propertyName.equalsIgnoreCase("behindPlayer"))   iBlockProperty = 5;

        return iBlockProperty;
    }
}
