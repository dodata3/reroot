#include "Context.h"

#include <cstdio>

#include "Keyboard.h"

Context* Context::sInstance = NULL;

Context& Context::Get()
{
    if (sInstance == NULL)
    {
        sInstance = new Context();
    }
    return *sInstance;
}

Context::Context()
{
    Init();
}

Context::~Context()
{

}

void Context::Init()
{
    #ifdef OS_WINDOWS

    #endif
    #ifdef OS_LINUX
        if ((mDisplay = XOpenDisplay(NULL)) == NULL) // Open Display according to environment variable
        {
            // error
			qDebug() << "Could not access X display\n";
        }
    #endif //OS_LINUX
}

void Context::Deinit()
{
	#ifdef OS_LINUX
        XCloseDisplay(mDisplay);
    #endif
}

QString Context::Title()
{
    #ifdef OS_WINDOWS
        HWND window = GetForegroundWindow();
        if (window == NULL)
        {
            return QString("");
        }
        int titleLength = GetWindowTextLength(window) * 2; // For some reason, SWTL seems to undershoot by some amount, just double it for now
        LPTSTR ctitle = new TCHAR[titleLength];
        DWORD copied = GetWindowText(window, ctitle, titleLength);

        //DEBUG
        printf("Title: %s\n", ctitle);

        QString qstitle(ctitle);
        return qstitle;
    #endif
}

QString Context::Executable()
{
    #ifdef OS_WINDOWS
        HWND window = GetForegroundWindow();
        if (window == NULL)
        {
            WindowsError("GetForegroundWindow");
            return QString("");
        }
        DWORD pid;
        LPDWORD ppid = &pid;
        GetWindowThreadProcessId(window, ppid);
        WindowsError("GetWindowThreadProcessId");

        const size_t nameLength = 256; // There doesn't seem to be any way of fetching the file name length, but beyond 256 seems excessive
        LPTSTR cname = new TCHAR[nameLength];

        //HANDLE handle = OpenProcess(PROCESS_QUERY_LIMITED_INFORMATION, FALSE, pid);
        HANDLE handle = OpenProcess(PROCESS_QUERY_INFORMATION, FALSE, pid);
        WindowsError("OpenProcess");
        DWORD copied = GetProcessImageFileName(handle, cname, nameLength);
        if (CloseHandle(handle) == 0)
        {
            WindowsError("GetProcessImageFileName");
        }

        printf("name: %s\n", cname);

        return QString(cname);
    #endif
	#ifdef OS_LINUX
		Window* window;
		int revert;

		XGetInputFocus(mDisplay, window, revert);

		Atom property = XInternAtom(mDisplay, "WM_NAME", False), type;
		int form;
		unsigned long remaining, length;
		unsigned char *list;

		if (XGetWindowProperty(mDisplay, window, property, 0, 1024, False, XA_STRING, &type, &form, &length, &remaining, &list) != Success)
		{
			//error
		}
		return QString((char*) list);
	#endif
}

int Context::ProcessID()
{
    #ifdef OS_WINDOWS
        HWND window = GetForegroundWindow();
        if (window == NULL)
        {
            return 0;
        }
        DWORD pid;
        LPDWORD ppid = &pid;
        GetWindowThreadProcessId(window, ppid);

        printf("pid: %i\n", int(pid));
        return int(pid);
    #endif

	

	#ifdef OS_LINUX
		Window* window;
		int revert;
		XWindowAttributes attr;

		XGetInputFocus(mDisplay, window, revert);
		//if (!XGetWindowProperty(mDisplay,

		XGetWindowAttributes(mDisplay, window, &attr);

		//return attr.
		return 0;
	#endif
}

void Context::SwitchContext()
{
	#ifdef OS_WINDOWS
		//Keyboard::Get().ModifierDown(Keyboard::ModAlt);
		Keyboard::Get().Down(ukey_alt_l);
		Keyboard::Get().Down(Ukey('\t'));
		Keyboard::Get().Up(Ukey('\t'));
		Keyboard::Get().Up(ukey_alt_l);
		//Keyboard::Get().ModifierUp(Keyboard::ModAlt);
	#endif // OS_WINDOWS
}