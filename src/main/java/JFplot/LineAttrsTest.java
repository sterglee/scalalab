/*
  KChart Utility
  Copyright (C) 2008  Marko Klopcic

  This program is free software; you can redistribute it and/or
  modify it under the terms of the GNU General Public License
  as published by the Free Software Foundation; either version 2
  of the License, or (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.


  You can contact me at Google mail with the following address: markok3.14
 */
package JFplot;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * This class contains unit tests for class LineAttrs.
 * 
 * @author Marko Klopcic, Apr. 2008
 */
public class LineAttrsTest
{
     
    public void testLineAttrsString() {
        LineAttrs la = new LineAttrs("squarey");
        assertEquals("square", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("", la.m_styleStr);

        la = new LineAttrs("square");
        assertEquals("square", la.m_markerStr);
        assertEquals("", la.m_colorStr);
        assertEquals("", la.m_styleStr);
        
        la = new LineAttrs("ysquarey");
        assertEquals("square", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("", la.m_styleStr);
        
        la = new LineAttrs("-.ysquarey");
        assertEquals("square", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("-.", la.m_styleStr);
        
        la = new LineAttrs("-ysquarey.");
        assertEquals("square", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("-", la.m_styleStr);
        
        la = new LineAttrs("");
        assertEquals("", la.m_markerStr);
        assertEquals("", la.m_colorStr);
        assertEquals("", la.m_styleStr);
        
        
        la = new LineAttrs("yo");
        assertEquals("o", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("", la.m_styleStr);

        la = new LineAttrs("k-.");
        assertEquals("", la.m_markerStr);
        assertEquals("k", la.m_colorStr);
        assertEquals("-.", la.m_styleStr);
        
        la = new LineAttrs("c.-");
        assertEquals(".", la.m_markerStr);
        assertEquals("c", la.m_colorStr);
        assertEquals("-", la.m_styleStr);
        
        la = new LineAttrs("y...");
        assertEquals(".", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("", la.m_styleStr);
        
        la = new LineAttrs("z");
        assertEquals("", la.m_markerStr);
        assertEquals("", la.m_colorStr);
        assertEquals("", la.m_styleStr);
        
        la = new LineAttrs("rg:b");
        assertEquals("", la.m_markerStr);
        assertEquals("r", la.m_colorStr);
        assertEquals(":", la.m_styleStr);
        
        la = new LineAttrs("o.-y");
        assertEquals("o", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("-", la.m_styleStr);
        
        la = new LineAttrs("hexagram--y");
        assertEquals("hexagram", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("--", la.m_styleStr);
        
        la = new LineAttrs(".o--y");
        assertEquals(".", la.m_markerStr);
        assertEquals("y", la.m_colorStr);
        assertEquals("--", la.m_styleStr);
    }

}
