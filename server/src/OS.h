// $Id$
// Description: OS Specific functions
// (C) Cyberpad Technologies 2011
#ifndef OS_H_
#define OS_H_

#include "Global.h"

#if defined(__WIN32__)
    #define OS_WINDOWS
    // Require Windows 2000+
    //#define _WIN32_WINNT 0x0500
    // Require Windows Vista+
    #define _WIN32_WINNT 0x0600
#elif defined(LINUX) || defined(__LINUX)
    #define OS_LINUX
#endif

#ifdef OS_WINDOWS
    #include <windows.h>
#endif

#endif // OS_H_


