import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from '../api/axios.jsx';

import tree from '../assets/images/communityTree.png';
import chunsik from '../assets/images/chunsik.jpg';
import CreateDetail from '../components/common/CreateDetail.jsx';
import SmallButton from '../components/button/SmallButton.jsx';

export default function CommunityWritePage() {
  const navigate = useNavigate();
  const [previewImage, setPreviewImage] = useState(chunsik);
  const [data, setData] = useState({
    title: '',
    content: '',
    treeSnapshot: null,
  });

  const handleClick = () => {
    const formData = new FormData();
    formData.append('title', data.title);
    formData.append('content', data.content);
    formData.append('treeSnapshot', data.treeSnapshot);

    axios
      .post('/community/insert', formData, {
        headers: { 'content-type': 'multipart/form-data' },
      })
      .then((res) => {
        console.log(res);
        navigate('/family/community');
      })
      .catch((err) => {
        console.log(err);
      });
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];

    if (file) {
      const reader = new FileReader();

      reader.onloadend = () => {
        setPreviewImage(reader.result);
        setData({
          ...data,
          treeSnapshot: file,
        });
      };
      reader.readAsDataURL(file);
    }
  };

  const handleChange = (e) => {
    setData({ ...data, [e.target.name]: e.target.value });
  };

  return (
    <div className="p-5 h-full">
      <h1 className="text-2xl font-bold">커뮤니티 글쓰기</h1>
      <div className="w-full h-1/2">
        <CreateDetail
          title={data.title}
          content={data.content}
          onInputChange={handleChange}
        />
      </div>
      <div className="w-full h-1/3 flex justify-around px-10">
        <div>
          <p className="mb-12">이미지 선택</p>
          <input
            type="file"
            onChange={handleFileChange}
            className="border-4 border-black p-1 rounded-md w-1/2"
          />
        </div>
        <div className="h-5/6">
          <p>미리보기</p>
          <img
            src={previewImage}
            alt="미리보기"
            className="h-40 w-40 object-contain rounded-md"
          />
        </div>
      </div>
      <div className="flex justify-end pr-4 pt-4" onClick={handleClick}>
        {/* 이후 버튼에 create 연결 */}
        <SmallButton text="작성" />
      </div>
    </div>
  );
}
