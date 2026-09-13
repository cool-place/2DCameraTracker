# Camera Tracking Simulator

Camera Tracking Simulator is a Java project that models a rotating camera scanning for and tracking a moving target while accounting for obstacles and line of sight.

The idea originally came from seeing a moving webcam mount and wondering how I could experiment with code that tracks another object. I only knew basic Java when I started, so I used this project as a way to learn real-time graphics, timers, geometry, and simulation logic.

## Features

* Rotating camera that continuously scans for a target
* Moving target with randomized movement
* Randomized target distance and obstacle placement
* Field-of-view detection
* Line-of-sight checking with obstacles
* Automatic target tracking after detection
* Start and stop controls
* Live tracking statistics

## Demo

<img width="800" height="511" alt="cameratrackingsimdemorec-ezgif com-video-to-gif-converter" src="https://github.com/user-attachments/assets/1a57abe7-29db-42aa-8c7e-f9293c9c6cf9" />

## How It Works

The simulation runs continuously using a Java Swing timer.

While scanning, the camera rotates through its field of view and compares its current angle with the angle of the moving target.

The simulator also checks whether any obstacles are blocking the line between the camera and target. If the target is inside the camera's field of view and has a clear line of sight, the camera detects it and begins tracking its movement.

If the target moves outside the field of view or becomes blocked, the camera loses the target and returns to scanning.

Each new simulation randomizes parts of the environment so the camera does not encounter the exact same scenario every time.

## Live Statistics

The simulator displays several values while it is running:

* Camera angle
* Target angle
* Target distance
* Target speed
* Target acquisitions
* Targets lost
* Total tracking time

These made it easier for me to see how the tracking logic was behaving while the simulation was running.

## Built With

* **Java**
* **Java Swing**
* **Java AWT**
* **Graphics2D**

## What I Learned

Before starting this project, I only had experience with basic Java.

While building it, I learned more about:

* Java Swing interfaces
* Swing timers
* Custom drawing with `Graphics2D`
* Angles and field-of-view calculations
* Line intersection
* Object movement and velocity
* Organizing a visual Java program across multiple classes

The project gave me a chance to use Java for something more visual and interactive than the programs I had previously written.

## Challenges

The most challenging part was getting the different pieces of the tracking logic to work together.

The camera has to continuously compare its direction with the target's position, determine whether or not the target is inside its field of view, check whether or not an obstacle blocks the line of sight, and switch between scanning and tracking while everything is moving.

Working through the angle calculations and updating everything in real time helped me understand how geometry can be used inside a visual program.

## Limitations

This is a simulation rather than a real computer vision system. The program already knows the positions of the camera, target, and obstacles and uses that information to model tracking behavior.

It does not currently detect objects from an actual camera feed.

## Future Improvements

Some things I would like to experiment with include:

* Smarter and more natural target movement
* More varied obstacle layouts
* Multiple moving targets
* Multiple cameras
* Improved tracking behavior
* Connecting the simulation to actual computer vision or hardware in the future
