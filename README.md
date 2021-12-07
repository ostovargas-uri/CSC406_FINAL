# CSC406_FINAL
Final Project for CSC 406 - Computer Graphics

This was my final project for my computer graphics course. My goal here was to recreate my favorite 3D modeling tool, Autodesk Maya.

I was not able to get the JAR to run on my computer as it has been several years. However, please feel free to run the source through an IDE such as Eclipse or Netbeans to build and view it for yourselves (safer route I suppose).

The controls are in the supplied PDF "CSC406_FinalManual".

Here I demonstrate my knowledge of 3D rendering and camera controls using the Processing library. I also wanted to find a way to select object in 3D and I couldn't wrap my head around raycasting at the time. So I went with a neat hack called color-buffer picking.

(Of course today I would go the raycasting route)

Essentially all 3D object are rendered with a unique ID mapped to an RGB color. When the mouse clicks, it reads the RGB value of that screen coordinate pixel and converts it to an integer ID.
