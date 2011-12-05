#include <QtGui>
#include "MainWindow.h"

#include "Mouse.h"
#include "Keyboard.h"

#ifdef _WIN32
#include <windows.h>
#define argc __argc
#define argv __argv
int WINAPI WinMain( HINSTANCE, HINSTANCE, LPSTR, int )
{
#else // _WIN32
int main( int argc, char *argv[] )
{
#endif // _WIN32

	Q_INIT_RESOURCE( RerootResources );

	QApplication a(argc, argv);

	// Some error checking -- Some OS's don't support systrays.
	if( !QSystemTrayIcon::isSystemTrayAvailable() ) {
		QMessageBox::critical( 0, QObject::tr( "Systray" ), QObject::tr( "I couldn't detect any system tray on this system." ) );
		return 1;
	}
	QApplication::setQuitOnLastWindowClosed( false );

	// Make sure all buttons and keys are released before closing
	atexit(Mouse::Clear);
	atexit(Keyboard::Clear);

	MainWindow w;
	return a.exec();
}
