// $Id$
// Description: Declares a cipher interface for objects to use
// (C) Cyberpad Technologies 2011

#include "Cipher.h"

void Cipher::Encrypt( SecretKey secretKey, QByteArray& ioData )
{
	// Encrypt the data using XOR encryption
}

bool Cipher::Decrypt( SecretKey secretKey, QByteArray& ioData )
{
	// Decrypt the data using XOR encryption techniques, return if decryption yielded meaningful data
	return true;
}
