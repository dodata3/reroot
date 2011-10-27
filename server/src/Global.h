// $Id$
// Description: Defines some global information
// (C) Cyberpad Technologies 2011
#ifndef GLOBAL_H_
#define GLOBAL_H_

#define REROOT_PORT 57110
typedef int SecretKey;

#if defined(__WIN32__)
    #define OS_WINDOWS
    // Require Windows 2000+
    //#define _WIN32_WINNT 0x0500
    // Require Windows Vista+
    #define _WIN32_WINNT 0x0600
#elif defined(LINUX) || defined(__LINUX)
    #define OS_LINUX
#endif


#endif // GLOBAL_H_
