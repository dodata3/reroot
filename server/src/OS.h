// $Id$
// Description: OS Specific functions
// (C) Cyberpad Technologies 2011
#ifndef OS_H_
#define OS_H_

#include <cstdio>

#include "Global.h"

#if defined(_WIN32)
    #define OS_WINDOWS
    // Require Windows 2000+
    //#define _WIN32_WINNT 0x0500
    // Require Windows Vista+
    #define _WIN32_WINNT 0x0600
#elif defined(LINUX) || defined(__LINUX) || defined( __linux ) || defined( __linux__ )
    //#define OS_LINUX
#else
    #error "Unsupported operating system"
#endif

#ifdef OS_WINDOWS
    #include <Windows.h>
    #include <Psapi.h>

    inline void WindowsError(const LPTSTR lpszFunction)
    {
        // Retrieve the system error message for the last-error code

        LPVOID lpMsgBuf;
        DWORD dw = GetLastError();
        if (dw == 0)
        {
            return;
        }

        FormatMessage(
            FORMAT_MESSAGE_ALLOCATE_BUFFER |
            FORMAT_MESSAGE_FROM_SYSTEM |
            FORMAT_MESSAGE_IGNORE_INSERTS,
            NULL,
            dw,
            MAKELANGID(LANG_NEUTRAL, SUBLANG_DEFAULT),
            (LPTSTR) &lpMsgBuf,
            0, NULL );

        printf("ERROR: %s(): %s\n", lpszFunction, lpMsgBuf);

        LocalFree(lpMsgBuf);
        //ExitProcess(dw);
    }
#endif // OS_WINDOWS

#endif // OS_H_


