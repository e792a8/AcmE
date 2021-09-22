package org.e792a8.acme.core.workspace;

public interface IDirectoryBuilder {
	IDirectoryBuilder setType(String type);

	IDirectoryBuilder setName(String name);

	IDirectoryBuilder setUrl(String url);
// TODO customization
//	IDirectoryBuilder setLang(String lang);

//	IDirectoryBuilder setJudge(String judge);

	IDirectoryBuilder setFileName(String fileName);

	IDirectory finish();
}
